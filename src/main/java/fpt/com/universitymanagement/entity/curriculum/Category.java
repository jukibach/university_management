package fpt.com.universitymanagement.entity.curriculum;

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

@Table(name = "categories", schema = "curriculum",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "name")
        })
public class Category extends BaseEntity {

    @Column(nullable = false)
    private String name;

    private String subjectType;

    @OneToMany(mappedBy = "category")
    private List<Course> courses;
}
