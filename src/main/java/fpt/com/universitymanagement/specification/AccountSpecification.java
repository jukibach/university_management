package fpt.com.universitymanagement.specification;

import fpt.com.universitymanagement.entity.Account;
import jakarta.persistence.criteria.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AccountSpecification implements Specification<Account> {
    private final String searchInput;
    
    public AccountSpecification(String searchInput) {
        this.searchInput = searchInput;
    }
    
    @Override
    public Predicate toPredicate(@NotNull Root<Account> root, @NotNull CriteriaQuery<?> query, @NotNull CriteriaBuilder criteriaBuilder) {
        root.join("roleAccounts", JoinType.LEFT).join("role", JoinType.LEFT);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("userName")), "%" + searchInput.toLowerCase() + "%"));
        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + searchInput.toLowerCase() + "%"));
        return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
    }
}
