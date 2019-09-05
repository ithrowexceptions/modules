package it.euris.group1.modules;
import it.euris.group1.modules.entities.Module;
import it.euris.group1.modules.entities.Type;
import it.euris.group1.modules.repositories.ModulesRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@TestConfiguration
public class TestConfig {

    @Bean
    public CommandLineRunner demo(ModulesRepository repository) {


        return args -> {
            //save some modules
            repository.save(new Module(1L, "Jason", "Smith", LocalDate.of(2000, 1, 1), Type.OWNER));
            repository.save(new Module(2L, "Alice", "Anderson", LocalDate.of(2000, 1, 1), Type.CHILD));
            repository.save(new Module(3L, "Bob", "Anderson", LocalDate.of(1980, 2, 2), Type.SPOUSE));
            repository.save(new Module(4L, "Bob", "Carlsen", LocalDate.of(1980, 3, 3), Type.SPOUSE));
            repository.save(new Module(5L, "Dean", "Dobro", LocalDate.of(2000, 4, 4), Type.CHILD));
            repository.save(new Module(6L, "Ester", "Effenbach", LocalDate.of(2010, 4, 5), Type.OWNER));
            repository.save(new Module(7L, "Filip", "Fjord", LocalDate.of(1950, 6, 6), Type.SPOUSE));
            repository.save(new Module(8L, "Greta", "Gunderson", LocalDate.of(1960, 7, 6), Type.CHILD));
            repository.save(new Module(9L, "Hans", "Haskell", LocalDate.of(1975, 8, 8), Type.OWNER));
            repository.save(new Module(10L, "Bob", "Anderson", LocalDate.of(1985, 9, 9), Type.SPOUSE));

        };


    }
}
