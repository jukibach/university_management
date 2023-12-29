package fpt.com.universitymanagement.repository;

import fpt.com.universitymanagement.entity.student.GradeReport;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GradeReportRepository extends JpaRepository<GradeReport, Long> {
    List<GradeReport> findAll(Specification<GradeReport> gradeReportSpec);
}
