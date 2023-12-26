package fpt.com.universitymanagement.repository;

import fpt.com.universitymanagement.entity.curriculum.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoursesRepository extends JpaRepository<Course, Long> {
}
