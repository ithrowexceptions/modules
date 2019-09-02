package it.euris.group1.modules.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModulesRepository  extends JpaRepository<Module,Long> {}
