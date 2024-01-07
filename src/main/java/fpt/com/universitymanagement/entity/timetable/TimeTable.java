package fpt.com.universitymanagement.entity.timetable;

import fpt.com.universitymanagement.entity.curriculum.Semester;
import fpt.com.universitymanagement.entity.student.Student;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "timetable", schema = "timetable")
public class TimeTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    private String dayOfWeek;

    @OneToMany(mappedBy = "timeTable",cascade = CascadeType.ALL)
    private List<TimeTableSession> timeTableSessions;

    @ManyToOne
    @JoinColumn(name = "semester_id", nullable = false)
    private Semester semester;

    @OneToMany(mappedBy = "timetable")
    private List<Student> students;

}
