package fpt.com.universitymanagement.controller;

import fpt.com.universitymanagement.dto.CoursesResponse;
import fpt.com.universitymanagement.dto.GradeReportResponse;
import fpt.com.universitymanagement.dto.InstructorResponse;
import fpt.com.universitymanagement.dto.StudentResponse;
import fpt.com.universitymanagement.entity.student.GradeReport;
import fpt.com.universitymanagement.entity.student.Student;
import fpt.com.universitymanagement.service.ApiResponseOperation;
import fpt.com.universitymanagement.service.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

import static fpt.com.universitymanagement.common.Constant.INSTRUCTOR_CONTROLLER;


@RestController
@RequestMapping(INSTRUCTOR_CONTROLLER)
public class InstructorController {

    @Autowired
    private InstructorService instructorService;


    @GetMapping("/courses")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiResponseOperation
    public ResponseEntity<Page<CoursesResponse>> getCourseList(
            @RequestParam String instructorCode,
            Pageable pageable) {
        return ResponseEntity.ok(instructorService.getCoursesByFindByCode(instructorCode, pageable));
    }

    @GetMapping("/students")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiResponseOperation
    public ResponseEntity<Page<StudentResponse>> studentList(
            @RequestParam Long id, Pageable pageable) {
        return ResponseEntity.ok(instructorService.getStudentByCourses(id, pageable));
    }

    @GetMapping("/gradeReport")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiResponseOperation
    public ResponseEntity<List<GradeReportResponse>> gradeReports(@RequestParam Long id) {
        return ResponseEntity.ok(instructorService.getGradeReportByStudentId(id));

    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ApiResponseOperation
    public ResponseEntity<GradeReport> updateGradeReport(@RequestBody GradeReport gradeReport) {
        GradeReport gradeReports = instructorService.updateGradeReport(gradeReport);
        return ResponseEntity.ok(gradeReports);
    }

    @GetMapping("/excel")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiResponseOperation
    @ResponseBody
    public ResponseEntity<InputStreamResource> exportStudentInCourse(@RequestParam Long id) {
        List<Student> students = instructorService.getStudentByCourses(id);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        instructorService.exportStudentsToExcel(students, out);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=students.xls");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(new ByteArrayInputStream(out.toByteArray())));
    }

    @GetMapping("/instructors")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<InstructorResponse>> searchInstructor(Pageable pageable, String searchInput) {
        return ResponseEntity.ok(instructorService.searchInstructor(pageable, searchInput));
    }

}
