package it.euris.group1.modules.controllers;

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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

//import net.sf.jasperreports.engine.*;
//import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
//import net.sf.jasperreports.engine.export.HtmlExporter;
//import net.sf.jasperreports.export.SimpleExporterInput;
//import net.sf.jasperreports.export.SimpleHtmlExporterOutput;

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

    @GetMapping("/surname/{surname}")
    public List<Module> getModulesBySurname(@PathVariable("surname") String moduleSurname) {
        return modulesRepository.findBySurname(moduleSurname);
    }

    @GetMapping("/birthdate/{birthdate}")
    public List<Module> getModulesByBirthdate(@PathVariable("birthdate") String moduleBirthdate) {
        LocalDate birthdate = LocalDate.parse(moduleBirthdate);
        return modulesRepository.findByBirthDate(birthdate);
    }

    @GetMapping("/timestamp/{creationTimestamp}")
    public List<Module> getModulesByTimestamp(@PathVariable("creationTimestamp") String moduleCreationTimestamp) {
        Timestamp timestamp = Timestamp.valueOf(moduleCreationTimestamp.replace('T', ' '));
//        Timestamp timestamp = Timestamp.valueOf(moduleCreationTimestamp);
        System.out.println(timestamp);
        return modulesRepository.findByCreationTimestamp(timestamp);
    }

    @GetMapping("/age/{age}")
    public List<Module> getModulesByAge(@PathVariable("age") Integer moduleAge) {
        return modulesRepository.findByAge(moduleAge);
    }

    @GetMapping("/type/{type}")
    public List<Module> getModulesByType(@PathVariable("type") String moduleType) throws ModuleNotFoundException {
        Type type;
        switch(moduleType) {
            case "OWNER": type = Type.OWNER; break;
            case "SPOUSE": type = Type.SPOUSE; break;
            case "CHILD": type = Type.CHILD; break;
            default: throw new ModuleNotFoundException();
        }
        return modulesRepository.findByType(type);
    }

    @GetMapping("/page")
    public List<Module> getModulePage(@RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "size", defaultValue = "5") int size) {
        Pageable sortedByName = PageRequest.of(page, size, Sort.by("name"));
        Page<Module> users = modulesRepository.findAll(sortedByName);
        List<Module> userEntities = users.getContent();
        return userEntities;
    }

    @GetMapping("/report/{id}")
    public ResponseEntity<byte[]> getReport(@PathVariable("id") Long id) throws ModuleNotFoundException, JRException {
        Module module = fetchModuleById(id);

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
                // Specify content type as PDF
                .header("Content-Type", "application/pdf; charset=UTF-8")
                // Tell browser to display PDF if it can
                .header("Content-Disposition", "inline; filename=\"module id:" + id + ".pdf\"")
                .body(bytes);
    }

/*
    @GetMapping("/report/{id}")
    public void getReport(@PathVariable("id") Long id, HttpServletResponse response) throws Exception {
        Module module = modulesRepository
                .findById(id)
                .orElseThrow(() -> new ModuleNotFoundException("Module not found for id: " + id));

        Map<String, Object> result = new HashMap<>();
        result.put("id", module.getId());
        result.put("name", module.getName());
        result.put("surname", module.getSurname());
        result.put("birthDate", module.getBirthDate());

        response.setContentType("application/pdf");

        InputStream inputStream = getClass().getResourceAsStream("/reports/module-report.jrxml.OLD");
        JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
//        JRSaver.saveObject(jasperReport, "employeeReport.jasper"); // To avoid compiling it every time, we can save it to a file
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(List.of(result));
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);

        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput("module.pdf"));
        SimplePdfReportConfiguration reportConfig
                = new SimplePdfReportConfiguration();
        reportConfig.setSizePageToContent(true);
        reportConfig.setForceLineBreakPolicy(false);
        SimplePdfExporterConfiguration exportConfig
                = new SimplePdfExporterConfiguration();
        exportConfig.setMetadataAuthor("gruppo1");
        exportConfig.setEncrypted(true);
        exportConfig.setAllowedPermissionsHint("PRINTING");
        exporter.setConfiguration(reportConfig);
        exporter.setConfiguration(exportConfig);
        exporter.exportReport();
//        HtmlExporter exporter = new HtmlExporter(DefaultJasperReportsContext.getInstance());
//        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
//        exporter.setExporterOutput(new SimpleHtmlExporterOutput(response.getWriter()));
//        exporter.exportReport();
    }
*/

    // ********** POST requests **********
    @PostMapping()
    public Module createModule(@Valid @RequestBody Module newModule) {
        return modulesRepository.save(newModule);
    }

    // ********** PUT requests **********
    @PutMapping()
    public Module updateModule(@Valid @RequestBody Module module) throws ModuleNotFoundException {
        Long id = module.getId();
        Module moduleToUpdate = fetchModuleById(id);

        moduleToUpdate.setName(module.getName());
        moduleToUpdate.setSurname(module.getSurname());
        moduleToUpdate.setAge(module.getAge());
        moduleToUpdate.setBirthDate(module.getBirthDate());
        moduleToUpdate.setCreationTimestamp(module.getCreationTimestamp());
        moduleToUpdate.setType(module.getType());

        Module updatedModule = modulesRepository.save(moduleToUpdate);

//        return ResponseEntity.ok(updatedModule);
        return updatedModule;
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
