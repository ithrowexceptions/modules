package it.euris.group1.modules.controllers.specifications;

import it.euris.group1.modules.entities.Module;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ModuleSpecification implements Specification<Module> {
    private Module module;

    public ModuleSpecification(Module module) {
        this.module = module;
    }

    @Override
    public Predicate toPredicate(Root<Module> root,
                                 CriteriaQuery<?> criteriaQuery,
                                 CriteriaBuilder criteriaBuilder) {
        Predicate predicate = criteriaBuilder.conjunction(); // creates a predicate disjunction (OR)

        if(module.getName() != null) {
            predicate.getExpressions().add(criteriaBuilder.equal(root.get("name"), module.getName()));
        }

        if(module.getSurname() != null) {
            predicate.getExpressions().add(criteriaBuilder.equal(root.get("surname"), module.getSurname()));
        }

        if(module.getAge() != null) {
            predicate.getExpressions().add(criteriaBuilder.equal(root.get("age"), module.getAge()));
        }

        return predicate;
    }
}
