package it.euris.group1.modules.repositories;

import it.euris.group1.modules.entities.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ModulesRepository  extends JpaRepository<Module,Long> {


        List<Module> findByName(String name);
        List<Module> findBySurname(String surname);
        List<Module> findByBirthDate(LocalDate birthDate);
        List<Module> findByAge(Integer age);
        List<Module> findByType (Enum type);
        //List<Module> findByTimestamp(Timestamp timestamp);


}
