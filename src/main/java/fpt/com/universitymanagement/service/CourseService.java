package fpt.com.universitymanagement.service;

import fpt.com.universitymanagement.dto.CoursesResponse;
import fpt.com.universitymanagement.entity.curriculum.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;
import java.util.List;

public interface CourseService {

    Page<CoursesResponse> getAllCourse(Pageable pageable);

    void exportCourseToExcel(List<Course> courses, OutputStream outputStream);

    List<Course> getAllCourses();

    Boolean hasCsvFormat(MultipartFile file);

    void processAndSaveData(MultipartFile file);
}
