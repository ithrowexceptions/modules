package it.euris.group1.modules;

import it.euris.group1.modules.entities.Module;
import it.euris.group1.modules.entities.Type;
import it.euris.group1.modules.repositories.ModulesRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RepositoryJpaTest {

    @Autowired
    ModulesRepository repository;

    @Test
    public void should_find_not_empty_repository() {
        List<Module> testList = repository.findAll();
        assertThat(testList).isNotEmpty();
    }

    @Test
    public void should_find_module_by_Name() {
        List<Module> prova  = repository.findByName("Jason");
        assertThat(prova.size()).isEqualTo(1);
    }

    @Test
    public void should_find_module_by_Id() {
        Module dean = new Module(5L,"Dean", "Dobro", LocalDate.of(2000, 4, 4), Timestamp.valueOf("2018-05-05 04:03:04.004"), 19, Type.CHILD);
        Module foundModule = repository.findById(5L).get();
        assertThat(foundModule.getId()).isEqualTo(dean.getId());
    }

    @Test
    public void should_find_module_by_surname() {
        List<Module> anderson = repository.findBySurname("Anderson");
        assertThat(anderson.size()).isEqualTo(3);
    }

    @Test
    public void should_find_module_by_birth_date() {
        List<Module> filip = repository.findByBirthDate(LocalDate.of(1950,06,06));
        assertThat(filip.get(0).getBirthDate()).isEqualTo(repository.findByName("Filip").get(0).getBirthDate());
    }

    @Test
    public void should_find_modules_by_age() {
        List<Module> ages = repository.findByAge(19);
        assertThat(ages.size()).isEqualTo(3);
    }

    @Test
    public void should_find_modules_by_TYPE() {
        List<Module> types = repository.findByType(Type.SPOUSE);
        assertThat(types.get(3)).isEqualTo(repository.findByAge(34).get(0));
    }


}

