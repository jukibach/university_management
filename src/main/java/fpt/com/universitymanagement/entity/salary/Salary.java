package fpt.com.universitymanagement.entity.salary;

import fpt.com.universitymanagement.entity.BaseEntity;
import fpt.com.universitymanagement.entity.department.Manager;
import fpt.com.universitymanagement.entity.faculty.Instructor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "salary", schema = "salary")
public class Salary  extends BaseEntity {

    @Column(nullable = false)
    private int baseSalary;

    @Column(nullable = false)
    private LocalDate payDate;

    @OneToMany(mappedBy = "salary")
    private List<Manager> managers;

    @OneToMany(mappedBy = "salary")
    private List<Instructor> instructors;
}
