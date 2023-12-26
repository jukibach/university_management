package fpt.com.universitymanagement.entity.building;

import fpt.com.universitymanagement.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "buildings", schema = "building")
@NoArgsConstructor
@AllArgsConstructor
public class Building extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "code", nullable = false,unique = true)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "details",nullable = false)
    private String details;


    @OneToMany(mappedBy = "building", cascade = CascadeType.ALL)
    private List<Room> rooms;
}
