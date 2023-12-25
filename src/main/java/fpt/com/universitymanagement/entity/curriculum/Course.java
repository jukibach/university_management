package fpt.com.universitymanagement.entity.curriculum;

import fpt.com.universitymanagement.entity.BaseEntity;
import fpt.com.universitymanagement.entity.faculty.CourseInstructor;
import fpt.com.universitymanagement.entity.student.StudentCourse;
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
@Table(name = "courses", schema = "curriculum",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "code")
        })
public class Course extends BaseEntity {

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int credits;

    @Column(nullable = false)
    private LocalDate startDay;

    @Column(nullable = false)
    private LocalDate startTime;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Exam> exams;

    @OneToMany(mappedBy = "course",cascade = CascadeType.ALL)
    private List<CurriculumCourse> curriculumCourses;

    @OneToMany(mappedBy = "course",cascade = CascadeType.ALL)
    private List<CourseInstructor> courseInstructors;

    @OneToMany(mappedBy = "course",cascade = CascadeType.ALL)
    private List<CoursePrerequisite> coursePrerequisites;

    @OneToMany(mappedBy = "course",cascade = CascadeType.ALL)
    private List<CourseSemester> courseSemesters;

    @OneToMany(mappedBy = "course",cascade = CascadeType.ALL)
    private List<Session> sessions;

    @OneToMany(mappedBy = "course",cascade = CascadeType.ALL)
    private List<StudentCourse> studentCourses;
}