package fpt.com.universitymanagement.entity.account;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "role_account", schema = "account")
@Getter
@Setter
public class RoleAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    
    @ManyToOne
    @JoinColumn(name = "role_id")
    Role role;
    
    @ManyToOne
    @JoinColumn(name = "account_id")
    Account account;
}
