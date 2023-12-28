package fpt.com.universitymanagement.repository;

import fpt.com.universitymanagement.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken>findByToken(String token);
}
