package fpt.com.universitymanagement.specification;

import fpt.com.universitymanagement.entity.account.Account;
import jakarta.persistence.criteria.*;
import lombok.NonNull;
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
    public Predicate toPredicate(@NonNull Root<Account> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder criteriaBuilder) {
        root.join("roleAccounts", JoinType.LEFT).join("role", JoinType.LEFT);
        if (searchInput != null) {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("userName")), "%" + searchInput.toLowerCase() + "%"));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + searchInput.toLowerCase() + "%"));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("createdBy")), "%" + searchInput.toLowerCase() + "%"));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("updatedBy")), "%" + searchInput.toLowerCase() + "%"));
            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        }
        return criteriaBuilder.conjunction();
    }
}
