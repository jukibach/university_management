package fpt.com.universitymanagement.entity.curriculum;

import fpt.com.universitymanagement.entity.BaseEntity;
import fpt.com.universitymanagement.entity.faculty.Instructor;
import fpt.com.universitymanagement.entity.student.StudentExam;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "exams", schema = "curriculum")
public class Exam extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String durationMinute;

    @ManyToOne
    @JoinColumn(name = "instructor_id", nullable = false)
    private Instructor instructor;

    @ManyToOne
    @JoinColumn(name = "semester_id", nullable = false)
    private Semester semester;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL)
    private List<StudentExam> studentExamScores;
}
