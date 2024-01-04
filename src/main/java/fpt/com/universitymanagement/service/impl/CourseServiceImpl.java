package fpt.com.universitymanagement.service.impl;

import fpt.com.universitymanagement.dto.CoursesResponse;
import fpt.com.universitymanagement.entity.curriculum.Course;
import fpt.com.universitymanagement.repository.CoursesRepository;
import fpt.com.universitymanagement.service.CourseService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CoursesRepository coursesRepository;
    private static final Logger log = LoggerFactory.getLogger(CourseServiceImpl.class);

    @Override
    public Page<CoursesResponse> getAllCourse(Pageable pageable) {
        Page<Course> courses = coursesRepository.findAll(pageable);
        return courses.map(this::convertToCourseDTO);
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
    public void exportCourseToExcel(List<Course> courses, OutputStream outputStream) {
        try (Workbook workbook = new HSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("courses");
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(1).setCellValue("id");
            headerRow.createCell(2).setCellValue("Code");
            headerRow.createCell(3).setCellValue("Name");
            headerRow.createCell(4).setCellValue("Credits");
            headerRow.createCell(5).setCellValue("Description");
            headerRow.createCell(6).setCellValue("StartTime");
            headerRow.createCell(7).setCellValue("StartDay");

            int rowNum = 1;
            for (Course courseResponse : courses) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(1).setCellValue(courseResponse.getId());
                row.createCell(2).setCellValue(courseResponse.getCode());
                row.createCell(3).setCellValue(courseResponse.getName());
                row.createCell(4).setCellValue(courseResponse.getCredits());
                row.createCell(5).setCellValue(courseResponse.getDescription());
                row.createCell(6).setCellValue(courseResponse.getStartTime());
                row.createCell(7).setCellValue(courseResponse.getStartDay());

            }

            workbook.write(outputStream);
        } catch (Exception e) {
            log.error("Error exporting Excel file:", e);
        }

    }

    @Override
    public List<Course> getAllCourses() {
        return coursesRepository.findAll();
    }

    @Override
    public Boolean hasCsvFormat(MultipartFile file) {
        String type = "text/csv";
        return type.equals(file.getContentType());
    }

    @Override
    public void processAndSaveData(MultipartFile file) {
        try {
            List<Course> courses = csvToCourses(file.getInputStream());
            coursesRepository.saveAll(courses);
        } catch (Exception e) {
            log.error("Error:", e);
        }
    }
 
    private List<Course> csvToCourses(InputStream inputStream) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
            List<Course> courses = new ArrayList<>();
            for (CSVRecord csvRecord : csvParser) {
                Course course = new Course(
                        Long.parseLong(csvRecord.get(0)),
                        csvRecord.get(1),
                        csvRecord.get(2),
                        csvRecord.get(3),
                        Integer.parseInt(csvRecord.get(4)),
                        LocalDate.parse(csvRecord.get(5)),
                        LocalDate.parse(csvRecord.get(6))
                );
                courses.add(course);
            }
            return courses;
        } catch (Exception e) {
            log.error("Error:", e);
        }
        return Collections.emptyList();
    }
}
