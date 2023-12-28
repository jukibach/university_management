package fpt.com.universitymanagement.service.impl;

import fpt.com.universitymanagement.entity.curriculum.Course;
import fpt.com.universitymanagement.entity.faculty.CourseInstructor;
import fpt.com.universitymanagement.entity.student.GradeReport;
import fpt.com.universitymanagement.entity.student.Student;
import fpt.com.universitymanagement.entity.student.StudentCourse;
import fpt.com.universitymanagement.repository.CoursesRepository;
import fpt.com.universitymanagement.repository.GradeReportRepository;
import fpt.com.universitymanagement.repository.InstructorsRepository;
import fpt.com.universitymanagement.repository.StudentRepository;
import fpt.com.universitymanagement.service.InstructorService;
import jakarta.persistence.criteria.Join;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.util.List;
import java.util.Optional;

@Service
public class InstructorServicelmpl implements InstructorService {

    @Autowired
    private InstructorsRepository instructorsRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CoursesRepository coursesRepository;
    @Autowired
    private GradeReportRepository gradeReportRepository;
    private static final Logger log = LoggerFactory.getLogger(InstructorServicelmpl.class);

    @Override
    public Page<Course> getCoursesByFindByCode(String code, Pageable pageable) {
        Specification<Course> courseInstructorSpec = (root, query, criteriaBuilder) -> {
            Join<Course, CourseInstructor> courseInstructorJoin = root.join("courseInstructors");
            return criteriaBuilder.equal(courseInstructorJoin.get("instructor").get("code"), code);
        };
        return coursesRepository.findAll(courseInstructorSpec, pageable);
    }

    @Override
    public Page<Student> getStudentByCourses(Long id, Pageable pageable) {
        Specification<Student> studentSpec = (root, query, criteriaBuilder) -> {
            Join<Student, StudentCourse> studentCourseJoin = root.join("studentCourses");
            return criteriaBuilder.equal(studentCourseJoin.get("course").get("id"), id);
        };
        return studentRepository.findAll(studentSpec, pageable);
    }

    @Override
    public List<Student> getStudentByCourses(Long id) {
        Specification<Student> studentSpec = (root, query, criteriaBuilder) -> {
            Join<Student, StudentCourse> studentCourseJoin = root.join("studentCourses");
            return criteriaBuilder.equal(studentCourseJoin.get("course").get("id"), id);
        };
        return studentRepository.findAll(studentSpec);
    }

    @Override
    public List<GradeReport> getGradeReportByStudentId(Long id) {
        Specification<GradeReport> gradeReportSpec = (root, query, criteriaBuilder) -> {
            Join<GradeReport, Student> gradeReportStudentJoin = root.join("student");
            return criteriaBuilder.equal(gradeReportStudentJoin.get("gradeReports").get("id"), id);
        };
        return gradeReportRepository.findAll(gradeReportSpec);
    }

    @Override
    public GradeReport updateGradeReport(Long studentId, GradeReport updatedGradeReport) {
        try {
            Optional<Student> studentOptional = studentRepository.findById(studentId);
            if (studentOptional.isPresent()) {
                List<GradeReport> gradeReports = studentOptional.get().getGradeReports();
                for (GradeReport gradeReport : gradeReports) {
                    if (gradeReport.getId().equals(updatedGradeReport.getId())) {
                        gradeReport.setPointProcess(updatedGradeReport.getPointProcess());
                        gradeReport.setPointEndCourse(updatedGradeReport.getPointEndCourse());
                        gradeReport.setTotalMark(updatedGradeReport.getTotalMark());
                        gradeReport.setGrades(updatedGradeReport.getGrades());
                        gradeReport.setCreatedAt(updatedGradeReport.getCreatedAt());
                        gradeReport.setCreatedBy(updatedGradeReport.getCreatedBy());
                        gradeReportRepository.save(gradeReport);
                        return gradeReport;
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error updating score report:", e);
        }
        return null;
    }

    @Override
    public void exportStudentsToExcel(List<Student> students, OutputStream outputStream) {

        try (Workbook workbook = new HSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Students");
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(1).setCellValue("id");
            headerRow.createCell(2).setCellValue("code");
            headerRow.createCell(3).setCellValue("name");
            headerRow.createCell(4).setCellValue("gender");
            headerRow.createCell(5).setCellValue("dob");
            headerRow.createCell(6).setCellValue("email");
            headerRow.createCell(7).setCellValue("phone");
            headerRow.createCell(8).setCellValue("address");
            headerRow.createCell(9).setCellValue("academicYear");

            int rowNum = 1;
            for (Student studentResponse : students) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(studentResponse.getId());
                row.createCell(1).setCellValue(studentResponse.getCode());
                row.createCell(2).setCellValue(studentResponse.getName());
                row.createCell(3).setCellValue(studentResponse.getGender());
                row.createCell(4).setCellValue(studentResponse.getDob());
                row.createCell(5).setCellValue(studentResponse.getEmail());
                row.createCell(6).setCellValue(studentResponse.getPhone());
                row.createCell(7).setCellValue(studentResponse.getAddress());
                row.createCell(8).setCellValue(studentResponse.getAcademicYear());

            }

            workbook.write(outputStream);

        } catch (Exception e) {
            log.error("Error exporting Excel file:", e);
        }
    }
}
