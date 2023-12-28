package fpt.com.universitymanagement.repository;

import fpt.com.universitymanagement.entity.faculty.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface InstructorsRepository extends JpaRepository<Instructor, Long> {

    Optional<Instructor> findByCode (String code);
}
