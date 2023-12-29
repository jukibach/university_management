package fpt.com.universitymanagement.service;

import fpt.com.universitymanagement.dto.CoursesResponse;
import fpt.com.universitymanagement.dto.GradeReportResponse;
import fpt.com.universitymanagement.dto.InstructorResponse;
import fpt.com.universitymanagement.dto.StudentResponse;
import fpt.com.universitymanagement.entity.student.GradeReport;
import fpt.com.universitymanagement.entity.student.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.OutputStream;
import java.util.List;

public interface InstructorService {

    Page<CoursesResponse> getCoursesByFindByCode(String code, Pageable pageable);

    InstructorResponse getInstructorByCode(String code);

    Page<StudentResponse> getStudentByCourses(Long id, Pageable pageable);

    List<Student> getStudentByCourses(Long id);

    List<GradeReportResponse> getGradeReportByStudentId(Long id);

    GradeReport updateGradeReport(GradeReport gradeReport);

    void exportStudentsToExcel(List<Student> students, OutputStream outputStream);

    Page<InstructorResponse> searchInstructor(Pageable pageable, String searchInput);
}
