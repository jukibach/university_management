package fpt.com.universitymanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "permissions", schema = "account")
@Getter
@Setter
public class Permission extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description")
    private String description;
    @OneToMany(mappedBy = "permission", fetch = FetchType.LAZY)
    Set<RolePermission> rolePermissions;
}
