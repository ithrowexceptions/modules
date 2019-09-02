package it.euris.group1.modules.controllers;

import it.euris.group1.modules.repositories.ModulesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/modules")
public class ModuleController {
    @Autowired
    private ModulesRepository modulesRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Module> getEmployeeById(@PathVariable("id") Long moduleId) throws Throwable {
        Module module = modulesRepository
                .findById(moduleId)
                .orElseThrow(() -> new ModuleNotFoundException("Module not found for id: " + moduleId));
        return ResponseEntity.ok().body(module);
    }
}
