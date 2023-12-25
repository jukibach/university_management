package fpt.com.universitymanagement.entity.student;

import fpt.com.universitymanagement.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "guardians", schema = "student")
public class Guardian extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String  relationships;

    @OneToMany(mappedBy = "guardian", cascade = CascadeType.ALL)
    private List<Student> students;
}
