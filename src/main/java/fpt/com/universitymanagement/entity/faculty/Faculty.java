package fpt.com.universitymanagement.entity.faculty;

import fpt.com.universitymanagement.entity.BaseEntity;
import fpt.com.universitymanagement.entity.curriculum.Curriculum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "faculties", schema = "faculty",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "code")
        })
public class Faculty extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    private String description;

    @OneToMany(mappedBy = "faculty", cascade = CascadeType.ALL)
    private List<Classes> classes;

    @OneToOne
    @JoinColumn(name = "curriculum_id", unique = true)
    private Curriculum curriculum;

    @OneToMany(mappedBy = "faculty")
    private List<Instructor> instructors;

}
