package fpt.com.universitymanagement.repository;

import fpt.com.universitymanagement.entity.student.GradeReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeReportRepository extends JpaRepository<GradeReport,Long> {
}
