package fpt.com.universitymanagement.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CoursesResponse {

    private String code;

    private String name;

    private String description;

    private int credits;

    private LocalDate startDay;

    private LocalDate startTime;
}
