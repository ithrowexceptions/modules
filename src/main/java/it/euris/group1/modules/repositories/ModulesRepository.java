package it.euris.group1.modules.repositories;

import it.euris.group1.modules.entities.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModulesRepository  extends JpaRepository<Module,Long> {}
