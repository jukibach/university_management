package fpt.com.universitymanagement.entity.faculty;

import fpt.com.universitymanagement.entity.BaseEntity;
import fpt.com.universitymanagement.entity.student.Student;
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
@Table(name = "classes", schema = "faculty",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "code")
        })
public class Classes extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;

    @OneToMany(mappedBy = "aClass")
    private List<Student> students;
}
