package fpt.com.universitymanagement.repository;

import fpt.com.universitymanagement.entity.student.GradeReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface GradeReportRepository extends JpaRepository<GradeReport, Long>, JpaSpecificationExecutor<GradeReport> {

}
