package fpt.com.universitymanagement.entity.curriculum;


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
@Table(name = "course_prerequisite", schema = "curriculum")
public class CoursePrerequisite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id",nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "prerquisite_id",nullable = false)
    private Prerequisite prerequisite;

}
