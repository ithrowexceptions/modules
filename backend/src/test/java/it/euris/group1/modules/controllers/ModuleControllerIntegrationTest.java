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

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ModulesApplication.class)
@AutoConfigureMockMvc
@Import(TestConfig.class)
public class ModuleControllerIntegrationTest {
    private static final String BASE_URL = "/modules";

    private static List<Module> mockModules = getMockModules();

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
        String formattedTimestamp = getFormattedTimestamp(mockModules.get(0).getCreationTimestamp());

        MvcResult result = mvc.perform(get(BASE_URL + "/{id}", mockModules.get(0).getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", comparesEqualTo(mockModules.get(0).getId().intValue())))
                .andExpect(jsonPath("$.name", is(mockModules.get(0).getName())))
                .andExpect(jsonPath("$.surname", is(mockModules.get(0).getSurname())))
                .andExpect(jsonPath("$.birthDate", is(mockModules.get(0).getBirthDate().toString())))
                .andExpect(jsonPath("$.creationTimestamp", is(formattedTimestamp)))
                .andExpect(jsonPath("$.age", is(mockModules.get(0).getAge())))
                .andExpect(jsonPath("$.type", is(mockModules.get(0).getType().toString())))
                .andReturn();

        // Assert JSON response's content with JSONAssert too for didactic purpose
        String jsonResult = result.getResponse().getContentAsString();
        JSONAssert.assertEquals("{id:" + mockModules.get(0).getId().intValue() +
                        ",name:\"" + mockModules.get(0).getName() +
                        "\",surname:\"" + mockModules.get(0).getSurname() +
                        "\",birthDate:\"" + mockModules.get(0).getBirthDate().toString() +
                        "\",creationTimestamp:\"" + formattedTimestamp +
                        "\",age:" + mockModules.get(0).getAge() +
                        ",type:\"" + mockModules.get(0).getType().toString() +
                        "\"}",
                jsonResult, true);
    }

    @Test
    public void whenModuleIDIsNotProvided_thenRetrieveAllModules() throws Exception {
        mvc.perform(get(BASE_URL)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(mockModules.size())));
//                .andReturn();
    }

    @Test
    public void whenModuleEntityIsPosted_saveItOnDb() throws Exception {
        Module postedModule = new Module("NewName", "NewSurname", LocalDate.of(1977, 5, 22), Type.CHILD);
        mockModules.add(postedModule);

        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        MvcResult result = mvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(postedModule)))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        JSONAssert.assertEquals("{name:\"" + postedModule.getName() +
                        "\",surname:\"" + postedModule.getSurname() +
                        "\",birthDate:\"" + postedModule.getBirthDate().toString() +
                        "\",type:\"" + postedModule.getType().toString() +
                        "\"}",
                jsonResult, false);

        result = mvc.perform(get(BASE_URL).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(mockModules.size())))
                .andReturn();
    }

    @Test
    public void whenPostingAModuleWithAnId_saveItOnDbWithANewId() throws Exception {
        Module postedModule = new Module("NewName", "NewSurname", LocalDate.of(1977, 5, 22), Type.CHILD);
        postedModule.setId(1L);
        mockModules.add(postedModule);

        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        MvcResult result = mvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(postedModule)))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        JSONAssert.assertNotEquals("{id:\"" + postedModule.getId() + "\"}", jsonResult, false);

        result = mvc.perform(get(BASE_URL).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(mockModules.size())))
                .andReturn();
    }

    @Test
    public void whenModuleEntityIsPutted_updateItOnDb() throws Exception {
        Module updatedModule = new Module(7L, "NewName", "NewSurname", LocalDate.of(1977, 5, 22), Type.CHILD);

        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        MvcResult result = mvc.perform(put(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(updatedModule)))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        JSONAssert.assertEquals("{id:" + updatedModule.getId().intValue() +
                        ",name:\"" + updatedModule.getName() +
                        "\",surname:\"" + updatedModule.getSurname() +
                        "\",birthDate:\"" + updatedModule.getBirthDate().toString() +
                        "\",age:" + updatedModule.getAge() +
                        ",type:\"" + updatedModule.getType().toString() +
                        "\"}",
                jsonResult, false);

        result = mvc.perform(get(BASE_URL).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(mockModules.size())))
                .andReturn();
    }

    @Test
    public void canDeleteEntityFromDb() throws Exception {
        MvcResult result = mvc.perform(delete(BASE_URL + "/{id}", mockModules.get(7).getId()))
                .andExpect(status().isAccepted())
                .andReturn();
        mockModules.remove(mockModules.get(7));

        mvc.perform(get(BASE_URL).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(mockModules.size())));
    }

    @Test
    public void whenModuledNameProvided_thenRetrievedTheCorrectModules() throws Exception {
        mvc.perform(get(BASE_URL + "/name/{name}", mockModules.get(2).getName())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", is(mockModules.get(2).getName())))
                .andExpect(jsonPath("$[1].name", is(mockModules.get(2).getName())))
                .andExpect(jsonPath("$[2].name", is(mockModules.get(2).getName())));
    }

    @Test
    public void whenWrongModuledNameProvided_thenReturn404() throws Exception {
        mvc.perform(get(BASE_URL + "/name/{name}", "Foo")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenModuledSurnameProvided_thenRetrievedTheCorrectModules() throws Exception {
        mvc.perform(get(BASE_URL + "/surname/{surname}", mockModules.get(4).getSurname())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].surname", is(mockModules.get(4).getSurname())));
    }

    @Test
    public void whenWrongModuledSurnameProvided_thenReturn404() throws Exception {
        mvc.perform(get(BASE_URL + "/surname/{surname}", "Foo")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenModuledBirthdateProvided_thenRetrievedTheCorrectModules() throws Exception {
        mvc.perform(get(BASE_URL + "/birthdate/{birthdate}", mockModules.get(0).getBirthDate())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].birthDate", is(mockModules.get(0).getBirthDate().toString())))
                .andExpect(jsonPath("$[1].birthDate", is(mockModules.get(0).getBirthDate().toString())));
    }

    @Test
    public void whenWrongModuledBirthdateProvided_thenReturn404() throws Exception {
        mvc.perform(get(BASE_URL + "/birthdate/{birthdate}", "1800-01-01")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    private static List<Module> getMockModules() {
        return new ArrayList<>(Arrays.asList(
                new Module(1L, "Jason", "Smith", LocalDate.of(2000, 1, 1), Timestamp.valueOf("2015-01-01 12:00:00.000"), 19, Type.OWNER),
                new Module(2L, "Alice", "Anderson", LocalDate.of(2000, 1, 1), Timestamp.valueOf("2019-02-02 01:01:01.001"), 19, Type.CHILD),
                new Module(3L, "Bob", "Anderson", LocalDate.of(1980, 2, 2), Timestamp.valueOf("2018-03-03 02:02:01.002"), 39, Type.SPOUSE),
                new Module(4L, "Bob", "Carlsen", LocalDate.of(1980, 3, 3), Timestamp.valueOf("2017-04-04 03:03:03.003"), 39, Type.SPOUSE),
                new Module(5L, "Dean", "Dobro", LocalDate.of(2000, 4, 4), Timestamp.valueOf("2018-05-05 04:03:04.004"), 19, Type.CHILD),
                new Module(6L, "Ester", "Effenbach", LocalDate.of(2010, 4, 5), Timestamp.valueOf("2017-06-06 15:05:05.005"), 9, Type.OWNER),
                new Module(7L, "Filip", "Fjord", LocalDate.of(1950, 6, 6), Timestamp.valueOf("2016-07-07 15:06:06.006"), 69, Type.SPOUSE),
                new Module(8L, "Greta", "Gunderson", LocalDate.of(1960, 7, 6), Timestamp.valueOf("2014-08-08 17:07:07.007"), 59, Type.CHILD),
                new Module(9L, "Hans", "Haskell", LocalDate.of(1975, 8, 8), Timestamp.valueOf("2013-09-09 12:00:00.000"), 44, Type.OWNER),
                new Module(10L, "Bob", "Anderson", LocalDate.of(1985, 9, 9), Timestamp.valueOf(" 2016-08-08 00:00:00.000"), 34, Type.SPOUSE)
        ));
    }

    private static String getFormattedTimestamp(Timestamp timestamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

        return timestamp
                .toLocalDateTime()
                .plusHours(1)
                .format(formatter)
                .replace(' ', 'T')
                .concat("+0200");
    }
}

