package fpt.com.universitymanagement.entity.curriculum;

import fpt.com.universitymanagement.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "prerequisites", schema = "curriculum")
public class Prerequisite extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    private String name;

    private String description;

    @OneToMany(mappedBy = "prerequisite",cascade = CascadeType.ALL)
    private List<CoursePrerequisite> coursePrerequisites;
}
