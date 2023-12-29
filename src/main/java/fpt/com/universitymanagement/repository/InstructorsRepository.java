package fpt.com.universitymanagement.repository;

import fpt.com.universitymanagement.entity.faculty.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;


public interface InstructorsRepository extends JpaRepository<Instructor, Long> {

}
