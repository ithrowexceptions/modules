package it.euris.group1.modules.controllers;

import it.euris.group1.modules.entities.Module;
import it.euris.group1.modules.entities.Type;
import it.euris.group1.modules.repositories.ModulesRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = ModuleController.class)
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

        var jason = Optional.of(mockModules.get(0));

//        when(modulesRepository.findById(1L)).thenReturn(jason);
        doReturn(jason).when(mockModulesRepository).findById(1L);
    }

    @Test
    public void contextLoad() {
        assertNotNull(mvc);
    }

    @Test
    public void whenModuledIsProvided_thenRetrievedNameIsCorrect() throws Exception {
        Long id = 1L;

        MvcResult result = mvc.perform(get(BASE_URL + "/{id}", id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is(1)))
//                .andExpect(jsonPath("$.name", is("Jason")))
//                .andExpect(jsonPath("$.surname", is("Smith")))
//                .andExpect(jsonPath("$.birthDate", is("2000-01-01")))
//                .andExpect(jsonPath("$.creationTimestamp", is("2015-01-01T11:00:00.000+0000")))
//                .andExpect(jsonPath("$.age", is(19)))
//                .andExpect(jsonPath("$.type", is("OWNER")))
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        System.out.println(jsonResult);

        JSONAssert.assertEquals("{id:1,name:\"Jason\"}",
                jsonResult,
                JSONCompareMode.LENIENT);
    }

    private List<Module> getMockModules() {
        return List.of(
                new Module(1L,"Jason","Smith", LocalDate.of(2000, 1, 1), Timestamp.valueOf("2015-01-01 12:00:00.000"), 19, Type.OWNER),
                new Module(2L,"Alice","Anderson", LocalDate.of(2000, 1, 1), Timestamp.valueOf("2019-02-02 01:01:01.001"), 19, Type.CHILD),
                new Module(3L,"Bob","Anderson", LocalDate.of(1980, 2, 2), Timestamp.valueOf("2018-03-03 02:02:01:002"), 39, Type.SPOUSE),
                new Module(4L,"Bob","Carlsen", LocalDate.of(1980, 3, 3), Timestamp.valueOf("2017-04-04 03:03:03:003"), 39, Type.SPOUSE),
                new Module(5L,"Dean","Dobro", LocalDate.of(2000, 4, 4), Timestamp.valueOf("2018-05-05 04:03:04.004"), 19, Type.CHILD),
                new Module(6L,"Ester","Effenbach", LocalDate.of(2010, 4, 5), Timestamp.valueOf("2017-06-06 15:05:05.005"), 9, Type.OWNER),
                new Module(7L,"Filip","Fjord", LocalDate.of(1950, 6, 6), Timestamp.valueOf("2016-07-07 15:06:06.006"), 69, Type.SPOUSE),
                new Module(8L,"Greta","Gunderson", LocalDate.of(1960, 7, 6), Timestamp.valueOf("2014-08-08 17:07:07.007"), 59, Type.CHILD),
                new Module(9L,"Hans","Haskell", LocalDate.of(1975, 8, 8), Timestamp.valueOf("2013-09-09 12:00:00.000"), 44, Type.OWNER),
                new Module(10L,"Bob","Anderson", LocalDate.of(1985, 9, 9), Timestamp.valueOf(" 2016-08-08 00:00:00.000"), 34, Type.SPOUSE)
        );
    }
}
