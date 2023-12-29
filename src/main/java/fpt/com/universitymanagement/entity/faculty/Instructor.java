package fpt.com.universitymanagement.entity.faculty;

import fpt.com.universitymanagement.entity.account.Account;
import fpt.com.universitymanagement.entity.BaseEntity;
import fpt.com.universitymanagement.entity.curriculum.Exam;
import fpt.com.universitymanagement.entity.curriculum.Session;
import fpt.com.universitymanagement.entity.salary.Salary;
import fpt.com.universitymanagement.entity.student.GradeReport;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "instructors", schema = "faculty",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "code"),
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "phone"),
        })
public class Instructor extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private LocalDate dob;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String address;

    @OneToMany(mappedBy = "instructor",cascade = CascadeType.ALL)
    private List<CourseInstructor> courseInstructors;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "salary_id")
    private Salary salary;

    @ManyToOne
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;

    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL)
    private List<Session> sessions;

    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL)
    private List<Exam> exams;

    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL)
    private List<GradeReport> gradeReports;
}
