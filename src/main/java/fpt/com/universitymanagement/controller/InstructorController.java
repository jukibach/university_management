package fpt.com.universitymanagement.controller;

import fpt.com.universitymanagement.dto.CoursesResponse;
import fpt.com.universitymanagement.entity.curriculum.Course;
import fpt.com.universitymanagement.entity.student.GradeReport;
import fpt.com.universitymanagement.entity.student.Student;
import fpt.com.universitymanagement.service.impl.InstructorServicelmpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import static fpt.com.universitymanagement.common.Constant.ACCOUNT_CONTROLLER;
@RestController
@RequestMapping(ACCOUNT_CONTROLLER)
public class InstructorController {

    @Autowired
    InstructorServicelmpl instructorServicelmpl;

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
        Page<Course> coursePage = instructorServicelmpl.getCoursesByFindByCode(instructorCode, pageable);
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
        Page<Student> students = instructorServicelmpl.getStudentByCourses(id, pageable);
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
        return instructorServicelmpl.getGradeReportByStudentId(id);
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
    public String updateGradeReport(@RequestParam Long studentId, @RequestBody GradeReport gradeReport) {
        return instructorServicelmpl.updateGradeReport(studentId, gradeReport);
    }


}
