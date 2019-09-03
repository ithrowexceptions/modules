package it.euris.group1.modules.controllers;

import it.euris.group1.modules.entities.Module;
import it.euris.group1.modules.repositories.ModulesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/modules")
public class ModuleController {
    @Autowired
    private ModulesRepository modulesRepository;

    @GetMapping()
    public List<Module> getModules() throws Exception {
        return modulesRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Module> getModuleById(@PathVariable("id") Long moduleId) throws ModuleNotFoundException {
        Module module = modulesRepository
                .findById(moduleId)
                .orElseThrow(() -> new ModuleNotFoundException("Module not found for id: " + moduleId));
        return ResponseEntity.ok().body(module);
    }

    @PostMapping()
    public Module createModule(@Valid @RequestBody Module newModule) {
        return modulesRepository.save(newModule);
    }

    @PutMapping()
    public ResponseEntity<Module> updateModule(@Valid @RequestBody Module module) throws ModuleNotFoundException {
        Long id = module.getId();

        Module moduleToUpdate = modulesRepository.findById(id).orElseThrow(() -> new ModuleNotFoundException("Module not found for id: " + id));

        moduleToUpdate.setName(module.getName());
        moduleToUpdate.setSurname(module.getSurname());
        moduleToUpdate.setAge(module.getAge());
        moduleToUpdate.setBirthDate(module.getBirthDate());
        moduleToUpdate.setCreationTimestamp(module.getCreationTimestamp());
        moduleToUpdate.setType(module.getType());

        Module updatedModule = modulesRepository.save(moduleToUpdate);

        return ResponseEntity.ok(updatedModule);
    }
}
