package fpt.com.universitymanagement.specification;

import fpt.com.universitymanagement.entity.faculty.Instructor;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class InstrucTorSpecification implements Specification<Instructor> {
    private final String searchInput;

    public InstrucTorSpecification(String searchInput) {
        this.searchInput = searchInput;
    }

    @Override
    public Predicate toPredicate(@NonNull Root<Instructor> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder criteriaBuilder) {
        if (searchInput != null) {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("code")), "%" + searchInput.toLowerCase() + "%"));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + searchInput.toLowerCase() + "%"));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("gender")), "%" + searchInput.toLowerCase() + "%"));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + searchInput.toLowerCase() + "%"));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("phone")), "%" + searchInput.toLowerCase() + "%"));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("address")), "%" + searchInput.toLowerCase() + "%"));
            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        }
        return criteriaBuilder.conjunction();
    }
}
