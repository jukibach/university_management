package fpt.com.universitymanagement.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "role_permission", schema = "account")
public class RolePermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    
    @ManyToOne
    @JoinColumn(name = "role_id")
    Role role;
    
    @ManyToOne
    @JoinColumn(name = "permission_id")
    Permission permission;
}
