package fpt.com.universitymanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "roles", schema = "account")
@Getter
@Setter
public class Role extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description")
    private String description;
    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    Set<RolePermission> rolePermissions;
    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    Set<RoleAccount> roleAccounts;
}
