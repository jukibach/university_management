package fpt.com.universitymanagement.entity.timetable;

import fpt.com.universitymanagement.entity.curriculum.Semester;
import fpt.com.universitymanagement.entity.student.Student;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "timetable_session", schema = "timetable")
public class TimeTable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private String dayOfWeek;

    @OneToMany(mappedBy = "timeTable",cascade = CascadeType.ALL)
    private List<TimeTableSession> timeTableSessions;

    @ManyToOne
    @JoinColumn(name = "semester_id", nullable = false)
    private Semester semester;

    @OneToMany(mappedBy = "timetable")
    private List<Student> students;

}
