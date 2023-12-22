package fpt.com.universitymanagement.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;

@MappedSuperclass
@Getter
@Setter
public class BaseEntity {
    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDate createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDate updatedAt;
    
    @Column(name = "created_by", updatable = false, nullable = false)
    private String createdBy;
    
    @Column(name = "updated_by")
    private String updatedBy;
}
