package fpt.com.universitymanagement.service.impl;

import fpt.com.universitymanagement.dto.CoursesResponse;
import fpt.com.universitymanagement.dto.StudentResponse;
import fpt.com.universitymanagement.entity.curriculum.Course;
import fpt.com.universitymanagement.entity.student.Student;
import fpt.com.universitymanagement.entity.student.StudentCourseGradeReport;
import fpt.com.universitymanagement.repository.CoursesRepository;
import fpt.com.universitymanagement.repository.StudentRepository;
import fpt.com.universitymanagement.service.StudentService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Join;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CoursesRepository coursesRepository;

    @Override
    public StudentResponse getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student with ID" + id + "not found"));
        return convertToStudentDTO(student);
    }

    private StudentResponse convertToStudentDTO(Student student) {
        StudentResponse studentResponse = new StudentResponse();
        studentResponse.setCode(student.getCode());
        studentResponse.setName(student.getName());
        studentResponse.setDob(student.getDob());
        studentResponse.setEmail(student.getEmail());
        studentResponse.setAcademicYear(student.getAcademicYear());
        studentResponse.setAddress(student.getAddress());
        studentResponse.setGender(student.getGender());
        studentResponse.setPhone(student.getPhone());
        return studentResponse;
    }

    @Override
    public List<CoursesResponse> getCourseByStudent(Long id) {
        Specification<Course> courseSpec = (root, query, criteriaBuilder) -> {
            Join<Course, StudentCourseGradeReport> studentCourseJoin = root.join("studentCourseGradeReports");
            return criteriaBuilder.equal(studentCourseJoin.get("student").get("id"), id);
        };
        List<Course> courses = coursesRepository.findAll(courseSpec);
        return courses.stream()
                .map(this::convertToCourseDTO)
                .collect(Collectors.toList());
    }

    private CoursesResponse convertToCourseDTO(Course course) {
        CoursesResponse courseDTO = new CoursesResponse();
        courseDTO.setCode(course.getCode());
        courseDTO.setName(course.getName());
        courseDTO.setCredits(course.getCredits());
        courseDTO.setDescription(course.getDescription());
        courseDTO.setStartTime(course.getStartTime());
        courseDTO.setStartDay(course.getStartDay());
        return courseDTO;
    }
}
