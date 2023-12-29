package fpt.com.universitymanagement.controller;

import fpt.com.universitymanagement.dto.CoursesResponse;
import fpt.com.universitymanagement.entity.curriculum.Course;
import fpt.com.universitymanagement.entity.student.GradeReport;
import fpt.com.universitymanagement.entity.student.Student;
import fpt.com.universitymanagement.service.InstructorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Get list of courses by instructor code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the course list",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CoursesResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid course ID provided"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<Course>> getCourseList(
            @RequestParam String instructorCode,
            Pageable pageable) {
        Page<Course> coursePage = instructorService.getCoursesByFindByCode(instructorCode, pageable);
        return ResponseEntity.ok(coursePage);
    }

    @GetMapping("/students")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get list of students by course ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the student list"),
            @ApiResponse(responseCode = "400", description = "Invalid course ID provided"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<Student>> studentList(
            @RequestParam Long id, Pageable pageable) {
        Page<Student> students = instructorService.getStudentByCourses(id, pageable);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/gradeReport")
    @Operation(summary = "Get grade reports by student ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the grade reports"),
            @ApiResponse(responseCode = "400", description = "Invalid student ID provided"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public List<GradeReport> gradeReports(@RequestParam Long id) {
        return instructorService.getGradeReportByStudentId(id);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update grade report for a student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the grade report"),
            @ApiResponse(responseCode = "400", description = "Invalid student ID or grade report provided"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public GradeReport updateGradeReport(@RequestBody GradeReport gradeReport) {
        return instructorService.updateGradeReport(gradeReport);
    }

    @GetMapping("/excel")
    @PreAuthorize("hasRole('ADMIN')")
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

}
