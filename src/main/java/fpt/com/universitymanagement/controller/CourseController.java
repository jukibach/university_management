package fpt.com.universitymanagement.controller;

import fpt.com.universitymanagement.dto.CoursesResponse;
import fpt.com.universitymanagement.dto.ResponseMessage;
import fpt.com.universitymanagement.entity.curriculum.Course;
import fpt.com.universitymanagement.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

import static fpt.com.universitymanagement.common.Constant.COURSE_CONTROLLER;

@RestController
@RequestMapping(COURSE_CONTROLLER)
public class CourseController {

    @Autowired
    CourseService courseService;

    @GetMapping("/courses")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<CoursesResponse>> getAllCourse(Pageable pageable) {
        return ResponseEntity.ok(courseService.getAllCourse(pageable));
    }

    @GetMapping("/excel")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public ResponseEntity<InputStreamResource> exportCourse() {
        List<Course> courses = courseService.getAllCourses();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        courseService.exportCourseToExcel(courses, out);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=course.xls");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(new ByteArrayInputStream(out.toByteArray())));
    }

    @PostMapping
    public ResponseEntity<ResponseMessage> upLoaFile(@RequestParam("file") MultipartFile file) {
        if (Boolean.TRUE.equals(courseService.hasCsvFormat(file))) {
            courseService.processAndSaveData(file);
            return ResponseEntity.ok(new ResponseMessage("Uploaded the file successfully : " + file.getOriginalFilename()));
        }
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage("Please upload csv file"));
    }
}
