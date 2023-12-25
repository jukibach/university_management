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
public class Building extends BaseEntity {

    @Column(nullable = false,unique = true)
    private String code;

    @Column(nullable = false,unique = true)
    private String name;

    @Column(nullable = false)
    private String details;


    @OneToMany(mappedBy = "building", cascade = CascadeType.ALL)
    private List<Room> rooms;
}
