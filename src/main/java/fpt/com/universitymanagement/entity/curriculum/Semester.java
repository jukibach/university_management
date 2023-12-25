package fpt.com.universitymanagement.entity.curriculum;

import fpt.com.universitymanagement.entity.BaseEntity;
import fpt.com.universitymanagement.entity.timetable.TimeTable;
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
@Table(name = "semester", schema = "curriculum")
public class Semester extends BaseEntity {

    private String name;

    private LocalDate startDate;

    private LocalDate endDate;

    @OneToMany(mappedBy = "semester", cascade = CascadeType.ALL)
    private List<CourseSemester> courseSemesters;

    @OneToMany(mappedBy = "semester",cascade = CascadeType.ALL)
    private List<TimeTable> timeTables;

    @ManyToOne
    @JoinColumn(name = "year_id",nullable = false)
    private Year year;

    @OneToMany(mappedBy = "semester", cascade = CascadeType.ALL)
    private List<Exam> exams;

}
