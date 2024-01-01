package fpt.com.universitymanagement.repository;

import fpt.com.universitymanagement.entity.account.LoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {
    List<LoginHistory> findByAccount_Id(long userName);
    
    List<LoginHistory> findByExpiryDateBeforeAndAccount_Id(LocalDateTime expiryDate, long accountId);
    
    Optional<LoginHistory> findByToken(String token);
    
    List<LoginHistory> findByAccount_IdOrderByCreatedAtDesc(long accountId, Pageable pageable);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM LoginHistory lh WHERE lh.status = 'FAIL' AND lh.account.id = :accountId")
    void deleteByStatusAndAccountId(long accountId);
}
