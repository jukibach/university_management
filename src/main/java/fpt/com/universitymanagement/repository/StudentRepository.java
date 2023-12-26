package fpt.com.universitymanagement.repository;

import fpt.com.universitymanagement.entity.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student,Long> {
}
