package fpt.com.universitymanagement.service;

import fpt.com.universitymanagement.dto.CoursesResponse;
import fpt.com.universitymanagement.dto.StudentResponse;

import java.util.List;

public interface StudentService {

    StudentResponse getStudentById(Long id);

    List<CoursesResponse> getCourseByStudent(Long id);
}
