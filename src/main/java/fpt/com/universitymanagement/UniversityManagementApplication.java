package fpt.com.universitymanagement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class UniversityManagementApplication {
    
    public static void main(String[] args) {
        log.info("Application start");
        SpringApplication.run(UniversityManagementApplication.class, args);
    }
    
}
