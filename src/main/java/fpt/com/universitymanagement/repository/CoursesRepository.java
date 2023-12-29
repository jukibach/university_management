package fpt.com.universitymanagement.repository;

import fpt.com.universitymanagement.entity.curriculum.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoursesRepository extends JpaRepository<Course, Long> {
    Page<Course> findAll(Specification<Course> courseInstructorSpec, Pageable pageable);
}
