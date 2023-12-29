package fpt.com.universitymanagement.entity.student;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fpt.com.universitymanagement.entity.BaseEntity;
import fpt.com.universitymanagement.entity.account.Account;
import fpt.com.universitymanagement.entity.faculty.Classes;
import fpt.com.universitymanagement.entity.timetable.TimeTable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "students", schema = "student",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "code"),
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "phone"),
        })
public class Student extends BaseEntity {

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

    @Column(nullable = false)
    private String academicYear;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "class_id")
    private Classes aClass;
    @JsonIgnore
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<StudentCourseGradeReport> studentCoursGradeReports;
    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "guardian_id")
    private Guardian guardian;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "timetable_id")
    private TimeTable timetable;
    @JsonIgnore
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<StudentExam> studentExam;

}
