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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.Optional;

import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
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

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ModulesRepository repo;

    @Test
    public void contextLoad() {
        assertNotNull(mvc);
    }

    @Test
    public void whenModuledIsProvided_thenRetrievedNameIsCorrect() throws Exception {
        MvcResult result = mvc.perform(get(BASE_URL + "/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", comparesEqualTo(1)))
                .andExpect(jsonPath("$.name", is("Jason")))
                .andExpect(jsonPath("$.surname", is("Smith")))
                .andExpect(jsonPath("$.birthDate", is("2000-01-01")))
//                .andExpect(jsonPath("$.creationTimestamp", is("2015-01-01T11:00:00.000+0000")))
                .andExpect(jsonPath("$.age", is(19)))
                .andExpect(jsonPath("$.type", is("OWNER")))
                .andReturn();

        // check JSON content with JSONAssert too, for didactic purpose
        String jsonResult = result.getResponse().getContentAsString();
        JSONAssert.assertEquals("{id:1,name:\"Jason\"," +
                        "surname:\"Smith\"," +
                        "birthDate:\"2000-01-01\"," +
//                        "creationTimestamp:\"2015-01-01T11:00:00.000+0000\"," +
                        "age:19," +
                        "type:\"OWNER\"}",
                jsonResult, false);
    }

    @Test
    public void whenModuleIsNotProvided_thenRetrieveAllModules() throws Exception {
        int size = repo.findAll().size();
        MvcResult result = mvc.perform(get(BASE_URL).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(size)))
                .andReturn();
    }

    @Test
    public void whenModuleEntityIsPosted_saveItOnDb() throws Exception {
        Module postedModule = new Module("NewName", "NewSurname", LocalDate.of(1977, 5, 22), Type.OWNER);

        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        MvcResult result = mvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(postedModule)))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        JSONAssert.assertEquals("{id:11," +
                        "name:\"NewName\"," +
                        "surname:\"NewSurname\"," +
                        "birthDate:\"1977-05-22\"," +
//                        "creationTimestamp:\"2015-01-01T11:00:00.000+0000\"," +
                        "age:42," +
                        "type:\"OWNER\"}",
                jsonResult, false);
    }

    @Test
    public void whenModuleEntityIsPutted_updateItOnDb() throws Exception {
        Module module = repo.findById(1L).orElseThrow(() -> new ModuleNotFoundException());

        Module updatedModule = new Module(module.getId(),
                "NewName",
                "NewSurname",
                LocalDate.of(1900, 01, 01),
                module.getCreationTimestamp(),
                119,
                module.getType());

        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        MvcResult result = mvc.perform(put(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(updatedModule)))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        JSONAssert.assertEquals("{id:1," +
                        "name:\"NewName\"," +
                        "surname:\"NewSurname\"," +
                        "birthDate:\"1900-01-01\"," +
//                        "creationTimestamp:\"2015-01-01T11:00:00.000+0000\"," +
                        "age:119," +
                        "type:\"OWNER\"}",
                jsonResult, false);
    }
}

