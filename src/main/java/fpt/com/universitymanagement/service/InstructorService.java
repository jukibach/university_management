package fpt.com.universitymanagement.service;

import fpt.com.universitymanagement.entity.curriculum.Course;
import fpt.com.universitymanagement.entity.student.GradeReport;
import fpt.com.universitymanagement.entity.student.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.OutputStream;
import java.util.List;

public interface InstructorService {

    Page<Course> getCoursesByFindByCode(String code, Pageable pageable);

    Page<Student> getStudentByCourses(Long id, Pageable pageable);

    List<Student> getStudentByCourses(Long id);

    List<GradeReport> getGradeReportByStudentId(Long id);

    GradeReport updateGradeReport(Long studentId, GradeReport gradeReport);

    void exportStudentsToExcel(List<Student> students, OutputStream outputStream);
}
