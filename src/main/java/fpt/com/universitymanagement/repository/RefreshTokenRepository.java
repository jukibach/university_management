package fpt.com.universitymanagement.repository;

import fpt.com.universitymanagement.entity.account.Account;
import fpt.com.universitymanagement.entity.account.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByToken(String token);
    
    @Modifying
    void deleteByAccount(Account account);
}
