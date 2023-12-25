package fpt.com.universitymanagement.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;


    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDate createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDate updatedAt;
    
    @CreatedBy
    @Column(name = "created_by", updatable = false, nullable = false)
    private String createdBy;
    
    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;
}
