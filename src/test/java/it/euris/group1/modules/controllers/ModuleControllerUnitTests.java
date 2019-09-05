package it.euris.group1.modules.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.euris.group1.modules.ModulesApplication;
import it.euris.group1.modules.entities.Module;
import it.euris.group1.modules.entities.Type;
import it.euris.group1.modules.repositories.ModulesRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("ciao")
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
        Long id = 1L;
        var jasonOptionalModule = Optional.of(mockModules.get(0));
        doReturn(jasonOptionalModule).when(mockModulesRepository).findById(id); // when(modulesRepository.findById(1L)).thenReturn(jason); non funziona

        var jasonModule = mockModules.get(0);
        MvcResult result = mvc.perform(get(BASE_URL + "/{id}", id)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
//                .andDo(print())
                .andExpect(jsonPath("$.id", comparesEqualTo(jasonModule.getId().intValue())))
                .andExpect(jsonPath("$.name", is(jasonModule.getName())))
//                .andExpect(jsonPath("$.surname", is(jasonModule.getSurname())))
//                .andExpect(jsonPath("$.birthDate", is(jasonModule.getBirthDate().toString())))
//                // TODO .andExpect(jsonPath("$.creationTimestamp", is(jasonModule.getCreationTimestamp()))) non funziona
//                .andExpect(jsonPath("$.age", is(jasonModule.getAge())))
//                .andExpect(jsonPath("$.type", is(jasonModule.getType().toString())))
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        System.out.println(jsonResult);

        JSONAssert.assertEquals("{id:1,name:\"Jason\"}",
                jsonResult,
                JSONCompareMode.LENIENT);
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
        Module postedModule = new Module(100L, "Added", "Zzz", LocalDate.of(2000, 1, 1), Timestamp.valueOf("2015-01-01 12:00:00.000"), 19, Type.OWNER);
        when(mockModulesRepository.save(any(Module.class))).thenReturn(postedModule);

        ObjectMapper om = new ObjectMapper();
        MvcResult result = mvc.perform(post(BASE_URL)
//                .contentType(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(om.writeValueAsString(SerializableModule.from(postedModule))))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void whenModuleEntityIsPutted_updateItOnDb() throws Exception {
        Module puttedModule = new Module(2L, "Alice", "Zzz", LocalDate.of(2000, 1, 1), Timestamp.valueOf("2019-02-02 01:01:01.001"), 19, Type.CHILD);
        when(mockModulesRepository.save(any(Module.class))).thenReturn(puttedModule);

        ObjectMapper om = new ObjectMapper();
        MvcResult result = mvc.perform(put(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(om.writeValueAsString(SerializableModule.from(puttedModule))))
                .andDo(print())
                .andExpect(status().isOk())
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

//@Component
class SerializableModule {
    private Long id;

    private String name;

    private String surname;

    private String birthDate;

    private String creationTimestamp;

    private Integer age;

    private Type type;

    private SerializableModule(Long id, String name, String surname, String birthDate, String creationTimestamp, Integer age, Type type) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.creationTimestamp = creationTimestamp;
        this.age = age;
        this.type = type;
    }

    static SerializableModule from(Module module) {
        return new SerializableModule(module.getId(),
                module.getName(),
                module.getSurname(),
                module.getBirthDate().toString(),
                module.getCreationTimestamp().toLocalDateTime().toString(),
                module.getAge(),
                module.getType());
    }
}
