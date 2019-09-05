package it.euris.group1.modules.controllers;

import it.euris.group1.modules.entities.Module;
import it.euris.group1.modules.repositories.ModulesRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/name/{name}")
    public List<Module> getModulesByName(@PathVariable("name") String moduleName) {
        return modulesRepository.findByName(moduleName);
    }

//    @GetMapping("/surname/{surname}")
//    public List<Module> getModulesBySurname(@PathVariable("surname") String moduleSurname) {
//        return modulesRepository.findBySurname(moduleSurname);
//    }
//
//    @GetMapping("/birthdate/{birthdate}")
//    public List<Module> getModulesByBirthdate(@PathVariable("birthdate") String moduleBirthdate) {
//        LocalDate birthdate = LocalDate.parse(moduleBirthdate);
//        return modulesRepository.findByBirthDate(birthdate);
//    }
//
//    @GetMapping("/timestamp/{creationTimestamp}")
//    public List<Module> getModulesByTimestamp(@PathVariable("creationTimestamp") String moduleCreationTimestamp) {
//        Timestamp timestamp = Timestamp.valueOf(moduleCreationTimestamp);
//        // TODO
//        return null;
//    }
//
//    @GetMapping("/age/{age}")
//    public List<Module> getModulesByAge(@PathVariable("creationTimestamp") Integer moduleAge) {
//        return modulesRepository.findByAge(moduleAge);
//    }
//
//    @GetMapping("/type/{type}")
//    public List<Module> getModulesByType(@PathVariable("type") String moduleType) {
//        // TODO
//        return null;
//    }

//    @GetMapping("/report/{id}")
//    public void getReport(@PathVariable("id") Long id, HttpServletResponse response) throws Exception {
//        Module module = modulesRepository
//                .findById(id)
//                .orElseThrow(() -> new ModuleNotFoundException("Module not found for id: " + id));
//
//        Map<String, Object> result = new HashMap<>();
//        result.put("id", module.getId());
//        result.put("name", module.getName());
//        result.put("surname", module.getSurname());
//        result.put("birthDate", module.getBirthDate());
//
//        response.setContentType("text/html");
//        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(List.of(result));
//        InputStream inputStream = this.getClass().getResourceAsStream("/reports/module-report.jrxml");
//        JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
//        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);
//        HtmlExporter exporter = new HtmlExporter(DefaultJasperReportsContext.getInstance());
//        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
//        exporter.setExporterOutput(new SimpleHtmlExporterOutput(response.getWriter()));
//        exporter.exportReport();
//    }

//    // ********** POST requests **********
//    @PostMapping()
//    public Module createModule(@Valid @RequestBody Module newModule) {
//        return modulesRepository.save(newModule);
//    }
//
//    // ********** PUT requests **********
//    @PutMapping()
//    public ResponseEntity<Module> updateModule(@Valid @RequestBody Module module) throws ModuleNotFoundException {
//        Long id = module.getId();
//
//        Module moduleToUpdate = fetchModuleById(id);
//
//        moduleToUpdate.setName(module.getName());
//        moduleToUpdate.setSurname(module.getSurname());
//        moduleToUpdate.setAge(module.getAge());
//        moduleToUpdate.setBirthDate(module.getBirthDate());
//        moduleToUpdate.setCreationTimestamp(module.getCreationTimestamp());
//        moduleToUpdate.setType(module.getType());
//
//        Module updatedModule = modulesRepository.save(moduleToUpdate);
//
//        return ResponseEntity.ok(updatedModule);
//    }
//
//    // ********** DELETE requests **********
//    @DeleteMapping("/{id}")
//    public ResponseEntity deleteModule(@PathVariable("id") Long id) throws ModuleNotFoundException {
//        Module module = fetchModuleById(id);
//
//        modulesRepository.delete(module);
//        return ResponseEntity.accepted().build();
//    }
//
//    private Module fetchModuleById(Long id) throws ModuleNotFoundException {
//        return modulesRepository.findById(id).orElseThrow(() -> new ModuleNotFoundException("Module not found for id: " + id));
//    }

    private Module fetchModuleById(Long id) throws ModuleNotFoundException {
        return modulesRepository.findById(id).get();
    }
}
