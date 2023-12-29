package fpt.com.universitymanagement.entity;

import fpt.com.universitymanagement.entity.account.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "password_conversion", schema = "account")
public class PasswordConversion extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    @OneToOne
    @JoinColumn
    private Account account;
    private Instant expiryDate;
}