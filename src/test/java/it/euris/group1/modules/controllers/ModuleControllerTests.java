package it.euris.group1.modules.controllers;

import it.euris.group1.modules.entities.Module;
import it.euris.group1.modules.more.Type;
import it.euris.group1.modules.repositories.ModulesRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
//@SpringBootTest
@WebMvcTest(value = ModuleController.class)
public class ModuleControllerTests {
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
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Jason")))
                .andExpect(jsonPath("$.surname", is("Smith")))
//                .andExpect(jsonPath("$.birthdate", is()))
//                .andExpect(jsonPath("$.creationTimestamp", is()))
                .andExpect(jsonPath("$.age", is(19)))
                .andExpect(jsonPath("$.type", is(Type.OWNER)))
                .andReturn();
    }

    private List<Module> getMockModules() {
        return List.of(new Module(1L,
                "Jason",
                "Smith",
                LocalDate.of(2000, 01, 01),
                Timestamp.valueOf("2015-01-01 12:00:00.000"),
                19,
                Type.OWNER));
    }
}
