package it.euris.group1.modules.services;

import it.euris.group1.modules.controllers.ModuleNotFoundException;
import it.euris.group1.modules.entities.Module;
import it.euris.group1.modules.repositories.ModulesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ModuleService {
    @Autowired
    private ModulesRepository modulesRepository;

    public List<Map<String, Object>> report(Long id) throws ModuleNotFoundException {
        Module module = modulesRepository
                .findById(id)
                .orElseThrow(() -> new ModuleNotFoundException("Module not found for id: " + id));

        Map<String, Object> result = new HashMap<>();
        result.put("id", module.getId());
        result.put("name", module.getName());
        result.put("surname", module.getSurname());
        result.put("birthDate", module.getBirthDate());
//        result.put("age", module.getAge());
//        result.put("creationTimestamp", module.getCreationTimestamp());
//        result.put("type", module.getType());

        return List.of(result);
    }
}
