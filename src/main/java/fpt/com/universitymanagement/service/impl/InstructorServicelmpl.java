package fpt.com.universitymanagement.service.impl;

import fpt.com.universitymanagement.dto.CoursesResponse;
import fpt.com.universitymanagement.dto.GradeReportResponse;
import fpt.com.universitymanagement.dto.InstructorResponse;
import fpt.com.universitymanagement.dto.StudentResponse;
import fpt.com.universitymanagement.entity.curriculum.Course;
import fpt.com.universitymanagement.entity.faculty.CourseInstructor;
import fpt.com.universitymanagement.entity.faculty.Instructor;
import fpt.com.universitymanagement.entity.student.GradeReport;
import fpt.com.universitymanagement.entity.student.Student;
import fpt.com.universitymanagement.entity.student.StudentCourseGradeReport;
import fpt.com.universitymanagement.repository.*;
import fpt.com.universitymanagement.service.InstructorService;
import fpt.com.universitymanagement.specification.InstrucTorSpecification;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.OutputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class InstructorServicelmpl implements InstructorService {

    @Autowired
    private InstructorsRepository instructorsRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CoursesRepository coursesRepository;
    @Autowired
    private GradeReportRepository gradeReportRepository;
    @Autowired
    private CourseInstructorsRepository courseInstructorsRepository;
    private static final Logger log = LoggerFactory.getLogger(InstructorServicelmpl.class);
    private static final String COURSE_LITERAL = "course";

    @Override
    public Page<CoursesResponse> getCoursesByFindByCode(String code, Pageable pageable) {
        Specification<Course> courseInstructorSpec = (root, query, criteriaBuilder) -> {
            Join<Course, CourseInstructor> courseInstructorJoin = root.join("courseInstructors");
            return criteriaBuilder.equal(courseInstructorJoin.get("instructor").get("code"), code);
        };
        Page<Course> coursePage = coursesRepository.findAll(courseInstructorSpec, pageable);
        return coursePage.map(this::convertToCourseDTO);
    }

    private CoursesResponse convertToCourseDTO(Course course) {
        CoursesResponse courseDTO = new CoursesResponse();
        courseDTO.setCode(course.getCode());
        courseDTO.setName(course.getName());
        courseDTO.setCredits(course.getCredits());
        courseDTO.setDescription(course.getDescription());
        courseDTO.setStartTime(course.getStartTime());
        courseDTO.setStartDay(course.getStartDay());
        return courseDTO;
    }

    @Override
    public Page<StudentResponse> getStudentByCourses(Long id, Pageable pageable) {
        Specification<Student> studentSpec = (root, query, criteriaBuilder) -> {
            Join<Student, StudentCourseGradeReport> studentCourseJoin = root.join("studentCourseGradeReports");
            return criteriaBuilder.equal(studentCourseJoin.get(COURSE_LITERAL).get("id"), id);
        };
        Page<Student> students = studentRepository.findAll(studentSpec, pageable);
        return students.map(this::convertToStudentDTO);
    }

    private StudentResponse convertToStudentDTO(Student student) {
        StudentResponse studentResponse = new StudentResponse();
        studentResponse.setCode(student.getCode());
        studentResponse.setName(student.getName());
        studentResponse.setDob(student.getDob());
        studentResponse.setEmail(student.getEmail());
        studentResponse.setAcademicYear(student.getAcademicYear());
        studentResponse.setAddress(student.getAddress());
        studentResponse.setGender(student.getGender());
        studentResponse.setPhone(student.getPhone());
        return studentResponse;
    }

    @Override
    public List<Student> getStudentByCourses(Long id) {
        Specification<Student> studentSpec = (root, query, criteriaBuilder) -> {
            Join<Student, StudentCourseGradeReport> studentCourseJoin = root.join("studentCourses");
            return criteriaBuilder.equal(studentCourseJoin.get(COURSE_LITERAL).get("id"), id);
        };
        return studentRepository.findAll(studentSpec);
    }

    @Override
    public List<GradeReportResponse> getGradeReportByStudentId(Long id) {
        Specification<GradeReport> gradeReportSpec = (root, query, criteriaBuilder) -> {
            Join<GradeReport, StudentCourseGradeReport> gradeReportStudentJoin = root.join("studentCourseGradeReport");
            return criteriaBuilder.equal(gradeReportStudentJoin.get("student").get("id"), id);
        };
        List<GradeReport> gradeReportList = gradeReportRepository.findAll(gradeReportSpec);
        return gradeReportList.stream()
                .map(this::convertToGradeDTO)
                .collect(Collectors.toList());
    }

    private GradeReportResponse convertToGradeDTO(GradeReport gradeReport) {
        GradeReportResponse gradeReportResponse = new GradeReportResponse();
        gradeReportResponse.setPointProcess(gradeReport.getPointProcess());
        gradeReportResponse.setPointEndCourse(gradeReport.getPointEndCourse());
        gradeReportResponse.setTotalMark(gradeReport.getTotalMark());
        gradeReportResponse.setGrades(gradeReport.getGrades());
        return gradeReportResponse;
    }

    @Override
    public GradeReportResponse updateGradeReport(GradeReport gradeReport) {
        if (gradeReport == null || gradeReport.getId() == null) {
            throw new IllegalArgumentException("GradeReport with ID must be provided");
        }
        Optional<GradeReport> existingGradeReport = gradeReportRepository.findById(gradeReport.getId());
        if (existingGradeReport.isEmpty()) {
            throw new EntityNotFoundException("GradeReport with ID " + gradeReport.getId() + " not found");
        }
        existingGradeReport.get().setPointProcess(gradeReport.getPointProcess());
        existingGradeReport.get().setPointEndCourse(gradeReport.getPointEndCourse());
        existingGradeReport.get().setTotalMark(gradeReport.getTotalMark());
        existingGradeReport.get().setGrades(gradeReport.getGrades());
        GradeReport updatedGradeReport = gradeReportRepository.save(existingGradeReport.get());
        return convertToGradeDTO(updatedGradeReport);
    }

    @Override
    public void exportStudentsToExcel(List<Student> students, OutputStream outputStream) {

        try (Workbook workbook = new HSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Students");
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(1).setCellValue("id");
            headerRow.createCell(2).setCellValue("code");
            headerRow.createCell(3).setCellValue("name");
            headerRow.createCell(4).setCellValue("gender");
            headerRow.createCell(5).setCellValue("dob");
            headerRow.createCell(6).setCellValue("email");
            headerRow.createCell(7).setCellValue("phone");
            headerRow.createCell(8).setCellValue("address");
            headerRow.createCell(9).setCellValue("academicYear");

            int rowNum = 1;
            for (Student studentResponse : students) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(studentResponse.getId());
                row.createCell(1).setCellValue(studentResponse.getCode());
                row.createCell(2).setCellValue(studentResponse.getName());
                row.createCell(3).setCellValue(studentResponse.getGender());
                row.createCell(4).setCellValue(studentResponse.getDob());
                row.createCell(5).setCellValue(studentResponse.getEmail());
                row.createCell(6).setCellValue(studentResponse.getPhone());
                row.createCell(7).setCellValue(studentResponse.getAddress());
                row.createCell(8).setCellValue(studentResponse.getAcademicYear());

            }

            workbook.write(outputStream);

        } catch (Exception e) {
            log.error("Error exporting Excel file:", e);
        }
    }

    @Override
    public InstructorResponse getInstructorById(Long id) {
        Instructor instructor = instructorsRepository.findById(id).orElse(null);
        if (instructor != null) {
            return convertToInstructorDTO(instructor);
        }
        return null;
    }

    private InstructorResponse convertToInstructorDTO(Instructor instructor) {
        InstructorResponse instructorResponse = new InstructorResponse();
        instructorResponse.setCode(instructor.getCode());
        instructorResponse.setName(instructor.getName());
        instructorResponse.setDob(instructor.getDob());
        instructorResponse.setEmail(instructor.getEmail());
        instructorResponse.setGender(instructor.getGender());
        instructorResponse.setPhone(instructor.getPhone());
        instructorResponse.setAddress(instructor.getAddress());
        return instructorResponse;
    }

    @Override
    public Page<InstructorResponse> searchInstructor(Pageable pageable, String searchInput) {
        InstrucTorSpecification instrucTorSpecification = new InstrucTorSpecification(searchInput);
        Page<Instructor> instructors = instructorsRepository.findAll(instrucTorSpecification, pageable);
        return instructors.map(this::convertToInstructorDTO);
    }

    @Override
    public InstructorResponse instructorUpdate(Instructor instructor) {
        if (instructor == null || instructor.getId() == null) {
            throw new IllegalArgumentException("GradeReport with ID must be provided");
        }
        Optional<Instructor> existinstructor = instructorsRepository.findById(instructor.getId());
        if (existinstructor.isEmpty()) {
            throw new EntityNotFoundException("GradeReport with ID " + instructor.getId() + " not found");
        }
        existinstructor.get().setCode(instructor.getCode());
        existinstructor.get().setDob(instructor.getDob());
        existinstructor.get().setName(instructor.getName());
        existinstructor.get().setGender(instructor.getGender());
        existinstructor.get().setEmail(instructor.getEmail());
        existinstructor.get().setAddress(instructor.getAddress());
        existinstructor.get().setPhone(instructor.getPhone());
        Instructor updateInstructor = instructorsRepository.save(existinstructor.get());
        return convertToInstructorDTO(updateInstructor);

    }


    @Override
    public String removeCourse(Long instructorId, Long courseId) {
        Specification<CourseInstructor> spec = Specification.where(
                (root, query, criteriaBuilder) -> {
                    Path<Long> instructorIdPath = root.get("instructor").get("id");
                    Path<Long> courseIdPath = root.get(COURSE_LITERAL).get("id");
                    return criteriaBuilder.and(
                            criteriaBuilder.equal(instructorIdPath, instructorId),
                            criteriaBuilder.equal(courseIdPath, courseId)
                    );
                }
        );

        long deletedCount = courseInstructorsRepository.delete(spec);
        return deletedCount > 0 ? "Delete Successfully" : "Delete Fail";
    }

}
