package it.euris.group1.modules.controllers;

import it.euris.group1.modules.entities.Module;
import it.euris.group1.modules.repositories.ModulesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/modules")
public class ModuleController {
    @Autowired
    private ModulesRepository modulesRepository;

    // ********** GET requests **********
    @GetMapping()
    public List<Module> getModules() {
        return modulesRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Module> getModuleById(@PathVariable("id") Long moduleId) throws ModuleNotFoundException {
        Module module = fetchModuleById(moduleId);
        return ResponseEntity.ok().body(module);
    }

    @GetMapping("/{name}")
    public List<Module> getModulesByName(@PathVariable("name") String moduleName) {
        // TODO
        return null;
    }

    @GetMapping("/{surname}")
    public List<Module> getModulesBySurname(@PathVariable("surname") String moduleSurname) {
        // TODO
        return null;
    }

    @GetMapping("/{birthdate}")
    public List<Module> getModulesByBirthdate(@PathVariable("birthdate") String moduleBirthdate) {
        LocalDate birthdate = LocalDate.parse(moduleBirthdate);
        // TODO
        return null;
    }

    @GetMapping("/{creationTimestamp}")
    public List<Module> getModulesByTimestamp(@PathVariable("creationTimestamp") String moduleCreationTimestamp) {
        Timestamp timestamp = Timestamp.valueOf(moduleCreationTimestamp);
        // TODO
        return null;
    }

    @GetMapping("/{age}")
    public List<Module> getModulesByAge(@PathVariable("creationTimestamp") Integer moduleAge) {
        // TODO
        return null;
    }

    @GetMapping("/{type}")
    public List<Module> getModulesByType(@PathVariable("type") String moduleType) {
        // TODO
        return null;
    }

    // ********** POST requests **********

    @PostMapping()
    public Module createModule(@Valid @RequestBody Module newModule) {
        return modulesRepository.save(newModule);
    }

    // ********** PUT requests **********
    @PutMapping()
    public ResponseEntity<Module> updateModule(@Valid @RequestBody Module module) throws ModuleNotFoundException {
        Long id = module.getId();

        Module moduleToUpdate = fetchModuleById(id);

        moduleToUpdate.setName(module.getName());
        moduleToUpdate.setSurname(module.getSurname());
        moduleToUpdate.setAge(module.getAge());
        moduleToUpdate.setBirthDate(module.getBirthDate());
        moduleToUpdate.setCreationTimestamp(module.getCreationTimestamp());
        moduleToUpdate.setType(module.getType());

        Module updatedModule = modulesRepository.save(moduleToUpdate);

        return ResponseEntity.ok(updatedModule);
    }

    // ********** DELETE requests **********
    @DeleteMapping("/{id}")
    public ResponseEntity deleteModule(@PathVariable("id") Long id) throws ModuleNotFoundException {
        Module module = fetchModuleById(id);

        modulesRepository.delete(module);
        return ResponseEntity.accepted().build();
    }

    private Module fetchModuleById(Long id) throws ModuleNotFoundException {
        return modulesRepository.findById(id).orElseThrow(() -> new ModuleNotFoundException("Module not found for id: " + id));
    }
}
