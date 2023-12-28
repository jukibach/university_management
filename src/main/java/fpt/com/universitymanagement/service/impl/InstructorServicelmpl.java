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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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
                Student student = studentOptional.get();
                List<GradeReport> gradeReports = student.getGradeReports();
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
            e.printStackTrace();
        }
        return null;
    }

}
