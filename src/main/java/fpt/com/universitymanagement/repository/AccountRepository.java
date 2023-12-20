package fpt.com.universitymanagement.repository;

import fpt.com.universitymanagement.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
}
