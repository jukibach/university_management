package fpt.com.universitymanagement.repository;

import fpt.com.universitymanagement.entity.curriculum.Course;
import fpt.com.universitymanagement.entity.student.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Page<Student> findAll(Specification<Student> studentSpec, Pageable pageable);

}
