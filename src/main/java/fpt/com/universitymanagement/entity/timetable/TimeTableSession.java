package fpt.com.universitymanagement.entity.timetable;

import fpt.com.universitymanagement.entity.curriculum.Session;
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
@Table(name = "timetable_session", schema = "timetable")
public class TimeTableSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "timetable_id",nullable = false)
    private TimeTable timeTable;

    @ManyToOne
    @JoinColumn(name = "session_id",nullable = false)
    private Session session;


}
