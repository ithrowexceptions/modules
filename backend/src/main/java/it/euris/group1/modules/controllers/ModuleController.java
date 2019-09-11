package it.euris.group1.modules.controllers;

import io.swagger.annotations.*;
import it.euris.group1.modules.controllers.specifications.ModuleSpecification;
import it.euris.group1.modules.entities.Module;
import it.euris.group1.modules.entities.Type;
import it.euris.group1.modules.repositories.ModulesRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.InputStream;
import java.net.URI;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/modules")
@Api(value="modules", description="Web API pertaining Module entities")
public class ModuleController {
    @Autowired
    private ModulesRepository modulesRepository;

    // ********** GET requests **********
    @GetMapping(produces = "application/json")
    @ApiOperation(value = "View a list of all models", response = Module.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved module list")
    })
    public ResponseEntity<List<Module>> getModules() {
        List<Module> modules = modulesRepository.findAll();
        return ResponseEntity.ok().body(modules);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    @ApiOperation(value = "Retrieve a module by ID", response = Module.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved module"),
            @ApiResponse(code = 404, message = "Invalid Module ID supplied")
    })
    public ResponseEntity<Module> getModuleById(
            @ApiParam(value = "Module ID", required = true)
            @PathVariable("id") Long moduleId) {
        Optional<Module> optModule = modulesRepository.findById(moduleId);
        if (optModule.isPresent())
            return ResponseEntity.ok().body(optModule.get());
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/name/{name}", produces = "application/json")
    @ApiOperation(value = "Retrieve a module by name", response = Module.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved module"),
            @ApiResponse(code = 404, message = "Module name not present in the database")
    })
    public ResponseEntity<List<Module>> getModulesByName(
            @ApiParam(value = "Module name", required = true)
            @PathVariable("name") String moduleName) {
        List<Module> modules = modulesRepository.findByName(moduleName);
        if (modules.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(modules);
    }

    @GetMapping("/surname/{surname}")
    public ResponseEntity<List<Module>> getModulesBySurname(@PathVariable("surname") String moduleSurname) {
        List<Module> modules = modulesRepository.findBySurname(moduleSurname);
        if (modules.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(modules);
    }

    @GetMapping("/birthdate/{birthdate}")
    public ResponseEntity<List<Module>> getModulesByBirthdate(@PathVariable("birthdate") String moduleBirthdate) {
        LocalDate birthdate = null;
        try {
            birthdate = LocalDate.parse(moduleBirthdate);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().build();
        }

        List<Module> modules = modulesRepository.findByBirthDate(birthdate);
        if (modules.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(modules);
    }

    @GetMapping("/timestamp/{creationTimestamp}")
    public ResponseEntity<List<Module>> getModulesByTimestamp(@PathVariable("creationTimestamp") String moduleCreationTimestamp) {
        Timestamp timestamp = null;
        try {
            timestamp = Timestamp.valueOf(moduleCreationTimestamp.replace('T', ' '));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

        List<Module> modules = modulesRepository.findByCreationTimestamp(timestamp);
        if (modules.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().body(modules);
    }

    @GetMapping("/age/{age}")
    public ResponseEntity<List<Module>> getModulesByAge(@PathVariable("age") Integer moduleAge) {
        List<Module> modules = modulesRepository.findByAge(moduleAge);
        if (modules.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().body(modules);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Module>> getModulesByType(@PathVariable("type") String moduleType) {
        Type type;
        switch (moduleType.toUpperCase()) {
            case "OWNER":
                type = Type.OWNER;
                break;
            case "SPOUSE":
                type = Type.SPOUSE;
                break;
            case "CHILD":
                type = Type.CHILD;
                break;
            default:
                return ResponseEntity.badRequest().build();
        }
        List<Module> modules = modulesRepository.findByType(type);
        if (modules.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().body(modules);
    }

    @GetMapping("/page")
    public Page<Module> getModulePage(@RequestParam(value = "page", defaultValue = "0") int page,
                                      @RequestParam(value = "size", defaultValue = "5") int size) {
        Pageable sortedByName = PageRequest.of(page, size, Sort.by("name"));
        return modulesRepository.findAll(sortedByName);
    }

    @GetMapping("/search")
    public Page<Module> searchModules(@RequestParam(name = "name", required = false) String moduleName,
                                      @RequestParam(name = "surname", required = false) String moduleSurname,
                                      @RequestParam(name = "birthdate", required = false) String moduleBirthdate,
                                      @RequestParam(name = "timestamp", required = false) String moduleCreationTimestamp,
                                      @RequestParam(name = "age", required = false) Integer moduleAge,
                                      @RequestParam(name = "type", required = false) String moduleType,
                                      Pageable page) throws ModuleNotFoundException {
        Module module = new Module();
        if (moduleName != null)
            module.setName(moduleName);
        if (moduleSurname != null)
            module.setSurname(moduleSurname);
        if (moduleAge != null)
            module.setAge(moduleAge);
        if (moduleBirthdate != null) {
            LocalDate birthdate = LocalDate.parse(moduleBirthdate);
            module.setBirthDate(birthdate);
        }
        if (moduleCreationTimestamp != null) {
            Timestamp timestamp = Timestamp.valueOf(moduleCreationTimestamp.replace('T', ' '));
            module.setCreationTimestamp(timestamp);
        }
        if (moduleAge != null) {
            module.setAge(moduleAge);
        }
        if (moduleType != null) {
            Type type;
            switch (moduleType.toUpperCase()) {
                case "OWNER":
                    type = Type.OWNER;
                    break;
                case "SPOUSE":
                    type = Type.SPOUSE;
                    break;
                case "CHILD":
                    type = Type.CHILD;
                    break;
                default:
            }
        }
        Specification<Module> specification = new ModuleSpecification(module);
        return modulesRepository.findAll(specification, page);
    }

    @GetMapping("/report/{id}")
    public ResponseEntity<byte[]> getReport(@PathVariable("id") Long moduleId) throws JRException {
        Optional<Module> optModule = modulesRepository.findById(moduleId);
        if (!optModule.isPresent())
            return ResponseEntity.notFound().build();

        Module module = optModule.get();

        Map<String, Object> params = new HashMap<>();
        params.put("id", module.getId());
        params.put("name", module.getName());
        params.put("surname", module.getSurname());
        params.put("birthDate", module.getBirthDate());
        params.put("creationTimestamp", module.getCreationTimestamp());
        params.put("age", module.getAge());
        params.put("type", module.getType());

        InputStream inputStream = getClass().getResourceAsStream("/reports/module-report.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(List.of(module));
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);
        byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);

        return ResponseEntity
                .ok()
                .header("Content-Type", "application/pdf; charset=UTF-8")
                .header("Content-Disposition", "inline; filename=\"module id:" + moduleId + ".pdf\"")
                .body(bytes);
    }

    // ********** POST requests **********
    @PostMapping(produces = "application/json")
    @ApiOperation(value = "Create a new module on the server", response = Module.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Module successfully created"),
    })
    public ResponseEntity<Module> createModule(@Valid @RequestBody Module newModule) {
        newModule.setCreationTimestamp();
        newModule.setAge();
        Module savedModule = modulesRepository.save(newModule);
        return ResponseEntity
                .created(URI.create("/modules/" + savedModule.getId()))
                .body(savedModule);
    }

    // ********** PUT requests **********
    @PutMapping(produces = "application/json")
    @ApiOperation(value = "Update a module on the server", response = Module.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Module successfully updated"),
            @ApiResponse(code = 404, message = "Invalid Module ID supplied"),
    })
    public ResponseEntity<Module> updateModule(
            @ApiParam(value = "Module with updated data", required = true)
            @Valid @RequestBody Module module) {
        Long id = module.getId();
        Optional<Module> optModule = modulesRepository.findById(id);
        if (!optModule.isPresent())
            return ResponseEntity.notFound().build();

        Module moduleToUpdate = optModule.get();
        moduleToUpdate.setName(module.getName());
        moduleToUpdate.setSurname(module.getSurname());
        moduleToUpdate.setBirthDate(module.getBirthDate());
        moduleToUpdate.setType(module.getType());
        moduleToUpdate.setAge();

        Module updatedModule = modulesRepository.save(moduleToUpdate);

        return ResponseEntity
                .created(URI.create("/modules/" + updatedModule.getId()))
                .body(updatedModule);
    }

    // ********** DELETE requests **********
    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete a module on the server")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Module deleted"),
            @ApiResponse(code = 404, message = "Invalid Module ID supplied"),
    })
    public ResponseEntity deleteModule(
            @ApiParam(value = "Module ID", required = true)
            @PathVariable("id") Long id) {
        Optional<Module> optModule = modulesRepository.findById(id);
        if (!optModule.isPresent())
            return ResponseEntity.notFound().build();

        modulesRepository.delete(optModule.get());
        return ResponseEntity.accepted().build();
    }

//    private Module fetchModuleById(Long id) throws ModuleNotFoundException {
//        return modulesRepository.findById(id).orElseThrow(() -> new ModuleNotFoundException("Module not found for id: " + id));
//    }
}
