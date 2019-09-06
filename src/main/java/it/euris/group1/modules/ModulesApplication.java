package it.euris.group1.modules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ModulesApplication {
    private static final Logger log = LoggerFactory.getLogger(ModulesApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ModulesApplication.class);
    }


}


