package fpt.com.universitymanagement.entity.department;

import fpt.com.universitymanagement.entity.BaseEntity;
import fpt.com.universitymanagement.entity.building.Room;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "departments", schema = "department")
public class Department extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @OneToMany(mappedBy = "department")
    private List<Manager> managers;

    @OneToMany(mappedBy = "department")
    private Set<Room> rooms;

}
