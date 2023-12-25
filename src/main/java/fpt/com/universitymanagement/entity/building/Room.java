package fpt.com.universitymanagement.entity.building;

import fpt.com.universitymanagement.entity.BaseEntity;
import fpt.com.universitymanagement.entity.curriculum.Session;
import fpt.com.universitymanagement.entity.department.Department;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "rooms", schema = "building")
public class Room extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String floor;

    @Column(nullable = false)
    private int capacity;


    @ManyToOne
    @JoinColumn(name = "building_id")
    private Building building;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<Session> sessions;
}
