package fpt.com.universitymanagement.service.impl;

import fpt.com.universitymanagement.entity.curriculum.Course;
import fpt.com.universitymanagement.entity.faculty.CourseInstructor;
import fpt.com.universitymanagement.entity.faculty.Instructor;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class InstructorServicelmpl implements InstructorService {

    @Autowired
    InstructorsRepository instructorsRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CoursesRepository coursesRepository;

    @Autowired
    GradeReportRepository gradeReportRepository;

//    @Override
//    public Page<Course> getCoursesByFindByCode(String code, Pageable pageable) {
//        Optional<Instructor> instructorOptional = instructorsRepository.findByCode(code);
//        if (instructorOptional.isPresent()) {
//            Instructor instructor = instructorOptional.get();
//            List<CourseInstructor> courseInstructors = instructor.getCourseInstructors();
//
//            List<Course> courses = new ArrayList<>();
//            for (CourseInstructor courseInstructor : courseInstructors) {
//                Course course = courseInstructor.getCourse();
//                courses.add(course);
//            }
//
//            return new PageImpl<>(courses, pageable, courses.size());
//        }
//
//        return Page.empty();
//    }

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
    public List<GradeReport> getGradeReportByStudentId(Long id) {
        Optional<Student> student = studentRepository.findById(id);
        if (student.isPresent()) {
            Student students = student.get();
            return students.getGradeReports();
        }
        return Collections.emptyList();
    }

    @Override
    public String updateGradeReport(Long studentId, GradeReport gradeReport) {
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isPresent()) {
            Student student1 = student.get();
            List<GradeReport> gradeReports = student1.getGradeReports();
            for (GradeReport gradeReport1 : gradeReports) {
                gradeReport1.setPointProcess(gradeReport.getPointProcess());
                gradeReport1.setPointEndCourse(gradeReport.getPointEndCourse());
                gradeReport1.setTotalMark(gradeReport.getTotalMark());
                gradeReport1.setGrades(gradeReport.getGrades());
                gradeReport1.setCreatedAt(gradeReport.getCreatedAt());
                gradeReport1.setCreatedBy(gradeReport.getCreatedBy());
                gradeReportRepository.save(gradeReport1);
            }
        }
        return "Update Successfully";
    }

}
