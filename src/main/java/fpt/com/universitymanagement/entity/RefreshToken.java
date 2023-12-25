package fpt.com.universitymanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "refresh_token", schema = "account")
public class RefreshToken extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @OneToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;
    
    @Column(nullable = false, unique = true, name = "token")
    private String token;
    
    @Column(nullable = false, name = "expiry_date")
    private Instant expiryDate;
}