package fpt.com.universitymanagement.repository;

import fpt.com.universitymanagement.entity.account.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {
    List<AccessToken> findByAccount_Id(long userName);
    
    List<AccessToken> findByExpiryDateBeforeAndAccount_Id(LocalDateTime expiryDate, long accountId);
    
    Optional<AccessToken> findByToken(String token);
    
}
