package fpt.com.universitymanagement.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class StudentResponse {

    private String code;
    private String name;
    private String gender;
    private LocalDate dob;
    private String email;
    private String phone;
    private String address;
    private String academicYear;
}
