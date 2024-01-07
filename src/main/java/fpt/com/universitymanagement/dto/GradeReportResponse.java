package fpt.com.universitymanagement.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GradeReportResponse {

    private double pointProcess;
    private double pointEndCourse;
    private double totalMark;
    private String grades;
}
