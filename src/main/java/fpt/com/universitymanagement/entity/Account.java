package fpt.com.universitymanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "accounts", schema = "account",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "user_name"),
                @UniqueConstraint(columnNames = "email")
        })
public class Account extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    @Column(name = "user_name", nullable = false)
    private String userName;
    @Column(name = "password", nullable = false)
    private String password;
    @Email
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "activated", nullable = false)
    private boolean activated;
    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    private Set<RoleAccount> roleAccounts;
}
