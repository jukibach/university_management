package fpt.com.universitymanagement.entity.student;

import fpt.com.universitymanagement.entity.curriculum.Course;
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
@Table(name = "student_course_grader", schema = "student",
        uniqueConstraints = @UniqueConstraint(columnNames = {"course_id", "student_id"}))
public class StudentCourseGradeReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;


    @OneToMany(mappedBy = "studentCourseGradeReport", cascade = CascadeType.ALL)
    private List<GradeReport> gradeReports;

}