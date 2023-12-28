package fpt.com.universitymanagement;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("fpt.com.universitymanagement.mapper")
@SpringBootApplication
public class UniversityManagementApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(UniversityManagementApplication.class, args);
    }
    
}
