package it.euris.group1.modules.controllers;


import it.euris.group1.modules.ModulesApplication;
import it.euris.group1.modules.repositories.ModulesRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.assertNotNull;

@ActiveProfiles("ciao")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ModulesApplication.class)
@AutoConfigureMockMvc
public class ModuleControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ModulesRepository repo;

    @Test
    public void contextLoad() {
        assertNotNull(mvc);
    }
}

