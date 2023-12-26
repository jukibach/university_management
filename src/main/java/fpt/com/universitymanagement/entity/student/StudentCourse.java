package fpt.com.universitymanagement.entity.student;

import fpt.com.universitymanagement.entity.curriculum.Course;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "student_course", schema = "student")
public class StudentCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id",nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id",nullable = false)
    private Course course;
}
