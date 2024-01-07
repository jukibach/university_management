package fpt.com.universitymanagement.repository;

import fpt.com.universitymanagement.entity.faculty.CourseInstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CourseInstructorsRepository extends JpaRepository<CourseInstructor, Long>, JpaSpecificationExecutor<CourseInstructor> {


}
