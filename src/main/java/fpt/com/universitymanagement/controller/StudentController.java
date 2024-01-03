package fpt.com.universitymanagement.controller;

import fpt.com.universitymanagement.dto.CoursesResponse;
import fpt.com.universitymanagement.dto.StudentResponse;
import fpt.com.universitymanagement.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static fpt.com.universitymanagement.common.Constant.STUDENT_CONTROLLER;

@RestController
@RequestMapping(STUDENT_CONTROLLER)
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/students")
    public ResponseEntity<StudentResponse> getStudentById(@RequestParam Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @GetMapping("/course")
    public ResponseEntity<List<CoursesResponse>> getCourseByStudent(@RequestParam Long id) {
        return ResponseEntity.ok(studentService.getCourseByStudent(id));
    }
}
