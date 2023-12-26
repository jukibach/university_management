package fpt.com.universitymanagement.entity.curriculum;

import fpt.com.universitymanagement.entity.BaseEntity;
import fpt.com.universitymanagement.entity.building.Room;
import fpt.com.universitymanagement.entity.faculty.Instructor;
import fpt.com.universitymanagement.entity.timetable.TimeTableSession;
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
@Table(name = "session", schema = "curriculum")
public class Session extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    private String name;

    private String timeStart;

    private String timeEnd;

    @ManyToOne
    @JoinColumn(name = "instructor_id", nullable = false)
    private Instructor instructor;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @OneToMany(mappedBy = "session",cascade = CascadeType.ALL)
    private List<TimeTableSession> timeTableSessions;
}
