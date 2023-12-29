package fpt.com.universitymanagement.repository;

import fpt.com.universitymanagement.entity.faculty.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface InstructorsRepository extends JpaRepository<Instructor, Long>, JpaSpecificationExecutor<Instructor> {
    Instructor findByCode(String code);


}
