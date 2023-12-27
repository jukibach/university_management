package fpt.com.universitymanagement.repository;

import fpt.com.universitymanagement.entity.account.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {
    boolean existsByAccount_UserName(String token);
    
    List<AccessToken> findByAccount_Id(long userName);
    
    List<AccessToken> findByExpiryDateBeforeAndAccount_Id(LocalDateTime expiryDate, long accountId);
}
