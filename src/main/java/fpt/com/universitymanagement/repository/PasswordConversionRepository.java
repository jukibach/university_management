package fpt.com.universitymanagement.repository;

import fpt.com.universitymanagement.entity.PasswordConversion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordConversionRepository extends JpaRepository<PasswordConversion, Long> {

    Optional<PasswordConversion> findByToken(String otp);
}
