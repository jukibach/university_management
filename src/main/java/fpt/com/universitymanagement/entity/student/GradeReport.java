package fpt.com.universitymanagement.entity.student;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fpt.com.universitymanagement.entity.BaseEntity;
import fpt.com.universitymanagement.entity.faculty.Instructor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "grade_report", schema = "student")
public class GradeReport extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    private double pointProcess;

    private double pointEndCourse;

    private double totalMark;

    private String grades;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "student_course_grader_id", nullable = false)
    private StudentCourseGradeReport studentCourseGradeReport;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "instructor_id", nullable = false)
    private Instructor instructor;
}
