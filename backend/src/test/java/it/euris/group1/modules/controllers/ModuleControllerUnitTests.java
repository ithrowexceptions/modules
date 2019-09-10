package it.euris.group1.modules.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.euris.group1.modules.ModulesApplication;
import it.euris.group1.modules.entities.Module;
import it.euris.group1.modules.entities.Type;
import it.euris.group1.modules.repositories.ModulesRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ModulesApplication.class)
@AutoConfigureMockMvc
//@RunWith(SpringRunner.class)
//@WebMvcTest(value = ModuleController.class)
public class ModuleControllerUnitTests {
    private List<Module> mockModules;
    private static final String BASE_URL = "/modules";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ModulesRepository mockModulesRepository;

    @Before
    public void setUp() {
        mockModules = getMockModules();
    }

    @Test
    public void contextLoad() {
        assertNotNull(mvc);
    }

    @Test
    public void whenModuledIsProvided_thenRetrievedNameIsCorrect() throws Exception {
        var jasonOptionalModule = Optional.of(mockModules.get(0));
        doReturn(jasonOptionalModule).when(mockModulesRepository).findById(1L);
        // when(modulesRepository.findById(1L)).thenReturn(jason); non funziona

        MvcResult result = mvc.perform(get(BASE_URL + "/{id}", 1L)
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

        // check JSON content with JSONAssert too
        String jsonResult = result.getResponse().getContentAsString();
        JSONAssert.assertEquals("{id:1,name:\"Jason\"," +
                        "surname:\"Smith\"," +
                        "birthDate:\"2000-01-01\"," +
                        "creationTimestamp:\"2015-01-01T13:00:00.000+0200\"," +
                        "age:19," +
                        "type:\"OWNER\"}",
                jsonResult, true);
    }

    @Test
    public void whenModuleIsNotProvided_thenRetrieveAllModules() throws Exception {
        when(mockModulesRepository.findAll()).thenReturn(mockModules);

        MvcResult result = mvc.perform(get(BASE_URL).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(getMockModules().size())))
                .andReturn();
    }

    @Test
    public void whenModuleEntityIsPosted_saveItOnDb() throws Exception {
        Module postedModule = new Module("NewName", "NewSurname", LocalDate.of(1977, 5, 22), Type.OWNER);
        Module savedModule = new Module(1L, "NewName", "NewSurname", LocalDate.of(1977, 5, 22), Type.OWNER);
        when(mockModulesRepository.save(any(Module.class))).thenReturn(savedModule);

        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        MvcResult result = mvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(postedModule)))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        JSONAssert.assertEquals("{id:1," +
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
        Module module = mockModules.get(0);
        var optionalModule = Optional.of(module);
        doReturn(optionalModule).when(mockModulesRepository).findById(1L);

        Module updatedModule = new Module(module.getId(),
                "NewName",
                module.getSurname(),
                module.getBirthDate(),
                module.getCreationTimestamp(),
                module.getAge(),
                module.getType());
        when(mockModulesRepository.save(any(Module.class))).thenReturn(updatedModule);

        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        MvcResult result = mvc.perform(put(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(updatedModule)))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        JSONAssert.assertEquals("{id:1," +
                        "name:\"NewName\"," +
                        "surname:\"Smith\"," +
                        "birthDate:\"2000-01-01\"," +
//                        "creationTimestamp:\"2015-01-01T11:00:00.000+0000\"," +
                        "age:19," +
                        "type:\"OWNER\"}",
                jsonResult, false);
    }

    @Test
    public void canDeleteEntityFromDb() throws Exception {
        Module module = mockModules.get(0);
        var optionalModule = Optional.of(module);
        doReturn(optionalModule).when(mockModulesRepository).findById(1L);
        doNothing().when(mockModulesRepository).delete(module);

        ObjectMapper om = new ObjectMapper();
        MvcResult result = mvc.perform(delete(BASE_URL + "/{id}", 1L))
                .andExpect(status().isAccepted())
                .andReturn();
    }

    private List<Module> getMockModules() {
        return List.of(
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
        );
    }
}
