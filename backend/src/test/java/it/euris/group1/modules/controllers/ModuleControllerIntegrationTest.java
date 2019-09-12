package it.euris.group1.modules.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.euris.group1.modules.ModulesApplication;
import it.euris.group1.modules.entities.Module;
import it.euris.group1.modules.entities.Type;
import it.euris.group1.modules.repositories.ModulesRepository;
import it.euris.group1.modules.repositories.TestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ModulesApplication.class)
@AutoConfigureMockMvc
@Import(TestConfig.class)
public class ModuleControllerIntegrationTest {
    private static final String BASE_URL = "/modules";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ModulesRepository repo;

    @Test
    public void contextLoad() {
        assertNotNull(mvc);
    }

    @Test
    // fetching id 1 must return Jason's module
    public void whenModuleIDIsProvided_thenRetrievedTheCorrectModule() throws Exception {
        mvc.perform(get(BASE_URL + "/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", comparesEqualTo(1)))
                .andExpect(jsonPath("$.name", is("Jason")))
                .andExpect(jsonPath("$.surname", is("Smith")))
                .andExpect(jsonPath("$.birthDate", is("2000-01-01")))
                .andExpect(jsonPath("$.creationTimestamp", is("2015-01-01T13:00:00.000+0200")))
                .andExpect(jsonPath("$.age", is(19)))
                .andExpect(jsonPath("$.type", is("OWNER")))
                .andReturn();
    }

    @Test
    public void whenModuleIDIsNotProvided_thenRetrieveAllModules() throws Exception {
        int repoSize = repo.findAll().size();

        mvc.perform(get(BASE_URL)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(repoSize)));
    }

    @Test
    public void whenModuledIdNotFound_thenReturn404() throws Exception {
        mvc.perform(get(BASE_URL + "/{id}", 1000)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void whenModuleEntityIsPosted_saveItOnDb() throws Exception {
        int repoSize = repo.findAll().size();

        Module module = new Module("NewName", "NewSurname", LocalDate.of(1977, 5, 22), Type.CHILD);
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        MvcResult result = mvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(module)))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        JSONAssert.assertEquals("{name:\"" + module.getName() +
                        "\",surname:\"" + module.getSurname() +
                        "\",birthDate:\"" + module.getBirthDate().toString() +
                        "\",type:\"" + module.getType().toString() +
                        "\"}",
                jsonResult, false);

        mvc.perform(get(BASE_URL).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(repoSize + 1)))
                .andReturn();
    }

    @Test
    public void whenPostingAModuleWithAnId_saveItOnDbWithANewId() throws Exception {
        int repoSize = repo.findAll().size();

        Module module = new Module("NewName", "NewSurname", LocalDate.of(1977, 5, 22), Type.CHILD);
        module.setId(1L);
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        MvcResult result = mvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(module)))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        JSONAssert.assertNotEquals("{id:\"" + module.getId() + "\"}", jsonResult, false);

        mvc.perform(get(BASE_URL).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(repoSize + 1)))
                .andReturn();
    }

    @Test
    public void whenModuleEntityIsPutted_updateItOnDb() throws Exception {
        int repoSize = repo.findAll().size();

        Module module = new Module(7L, "NewName", "NewSurname", LocalDate.of(1977, 5, 22), Type.CHILD);
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        MvcResult result = mvc.perform(put(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(module)))
                .andExpect(status().isCreated())
                .andReturn();

        Module updatedModule = repo.findById(7L).get();

        String jsonResult = result.getResponse().getContentAsString();
        JSONAssert.assertEquals("{id:" + updatedModule.getId().intValue() +
                        ",name:\"" + updatedModule.getName() +
                        "\",surname:\"" + updatedModule.getSurname() +
                        "\",birthDate:\"" + updatedModule.getBirthDate().toString() +
                        "\",creationTimestamp:\"" + "2016-07-07T15:06:06.006+0200" +
                        "\",age:" + updatedModule.getAge() +
                        ",type:\"" + updatedModule.getType().toString() +
                        "\"}",
                jsonResult, false);

        mvc.perform(get(BASE_URL).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(repoSize)))
                .andReturn();
    }

    @Test
    public void canDeleteEntityFromDb() throws Exception {
        int repoSize = repo.findAll().size();

        mvc.perform(delete(BASE_URL + "/{id}", 6))
                .andExpect(status().isAccepted())
                .andReturn();

        mvc.perform(get(BASE_URL).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(repoSize - 1)));
    }

    @Test
    public void whenModuledNameProvided_thenRetrievedTheCorrectModules() throws Exception {
        mvc.perform(get(BASE_URL + "/name/{name}", "Bob")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", is("Bob")))
                .andExpect(jsonPath("$[1].name", is("Bob")))
                .andExpect(jsonPath("$[2].name", is("Bob")));
    }

    @Test
    public void whenModuledNameNotFound_thenReturn404() throws Exception {
        mvc.perform(get(BASE_URL + "/name/{name}", "Foo")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenModuledSurnameProvided_thenRetrievedTheCorrectModules() throws Exception {
        mvc.perform(get(BASE_URL + "/surname/{surname}", "Smith")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].surname", is("Smith")));
    }

    @Test
    public void whenModuledSurnameNotFound_thenReturn404() throws Exception {
        mvc.perform(get(BASE_URL + "/surname/{surname}", "Foo")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenModuledBirthdateProvided_thenRetrievedTheCorrectModules() throws Exception {
        mvc.perform(get(BASE_URL + "/birthdate/{birthdate}", "2000-01-01")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].birthDate", is("2000-01-01")))
                .andExpect(jsonPath("$[1].birthDate", is("2000-01-01")));
    }

    @Test
    public void whenModuledBirthdateNotFound_thenReturn404() throws Exception {
        mvc.perform(get(BASE_URL + "/creationTimestamp/{creationTimestamp}", "1800-01-01")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

//    @Test
//    public void whenModuledTimestampProvided_thenRetrievedTheCorrectModules() throws Exception {
//        mvc.perform(get(BASE_URL + "/creationTimestamp/2015-01-01T12:00:00.000")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(1)))
//                .andExpect(jsonPath("$[0].creationTimestamp", is("2015-01-01T12:00:00.000+0200")));
//    }
//
//    @Test
//    public void whenWrongModuledTimestampProvided_thenReturn404() throws Exception {
//        mvc.perform(get(BASE_URL + "/creationTimestamp/{creationTimestamp}", "1800-01-01T12:00:00.000+0200\"")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound());
//    }

    @Test
    public void whenModuledAgeProvided_thenRetrievedTheCorrectModules() throws Exception {
        mvc.perform(get(BASE_URL + "/age/{age}", 19)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].age", is(19)))
                .andExpect(jsonPath("$[1].age", is(19)))
                .andExpect(jsonPath("$[2].age", is(19)));
    }

    @Test
    public void whenModuledAgeNotFound_thenReturn404() throws Exception {
        mvc.perform(get(BASE_URL + "/age/{age}", 1000)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenModuledTypeProvided_thenRetrievedTheCorrectModules() throws Exception {
        mvc.perform(get(BASE_URL + "/type/{type}", "OWNER")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].type", is("OWNER")))
                .andExpect(jsonPath("$[1].type", is("OWNER")))
                .andExpect(jsonPath("$[2].type", is("OWNER")));
    }

    @Test
    public void whenWrongModuledTypeProvided_thenReturn400() throws Exception {
        mvc.perform(get(BASE_URL + "/type/{type}", "Foo")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenFetchingPage0_thenGetPage0() throws Exception {
        mvc.perform(get(BASE_URL + "/page").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number", is(0)))
                .andExpect(jsonPath("$.content", hasSize(5)))
                .andExpect(jsonPath("$.pageable.sort.sorted", is(true)))
                .andExpect(jsonPath("$.content[0].name", is("Alice")))
                .andExpect(jsonPath("$.pageable.pageSize", is(5)));
    }

    @Test
    public void whenFetchingPageWithParameters_thenGetCorrectPage() throws Exception {
        mvc.perform(get(BASE_URL + "/page?page=1&size=2").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number", is(1)))
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.pageable.pageSize", is(2)));
    }

//    @Test
//    public void canGetReportOfModuleWithID1() throws Exception {
//        mvc.perform(get(BASE_URL + "/report/1").accept(MediaType.APPLICATION_PDF))
//                .andExpect(status().isOk());
//    }

    @Test
    public void canSearhAModuleFilteringByProperties() throws Exception {
        mvc.perform(get(BASE_URL + "/search?surname=Anderson&age=19").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id", comparesEqualTo(2)))
                .andExpect(jsonPath("$.content[0].name", is("Alice")))
                .andExpect(jsonPath("$.content[0].surname", is("Anderson")))
                .andExpect(jsonPath("$.content[0].birthDate", is("2000-01-01")))
                .andExpect(jsonPath("$.content[0].creationTimestamp", is("2019-02-02T02:01:01.001+0200")))
                .andExpect(jsonPath("$.content[0].age", is(19)))
                .andExpect(jsonPath("$.content[0].type", is("CHILD")));
    }
}

