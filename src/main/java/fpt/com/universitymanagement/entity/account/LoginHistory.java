package fpt.com.universitymanagement.entity.account;

import fpt.com.universitymanagement.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "login_history", schema = "account",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "token")
        })
public class LoginHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @Column(name = "token")
    private String token;
    
    @Column(name = "status", nullable = false)
    private String status;
    
    @Column(name = "ip_address", nullable = false)
    private String ipAddress;
    
    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;
}