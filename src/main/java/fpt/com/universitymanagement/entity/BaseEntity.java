package fpt.com.universitymanagement.entity;


import fpt.com.universitymanagement.common.SecurityUtils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;
import java.time.Instant;

@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
    @Column(name = "created_at", updatable = false, nullable = false)
    private Timestamp createdAt;
    
    @Column(name = "created_by", updatable = false, nullable = false)
    private String createdBy;
    
    @Column(name = "updated_at")
    private Timestamp updatedAt;
    
    @Column(name = "updated_by")
    private String updatedBy;
    
    @PrePersist
    private void onCreate() {
        if (this.createdBy == null) {
            this.createdBy = SecurityUtils.getAuthentication();
            this.createdAt = Timestamp.from(Instant.now());
        }
    }
    
    @PreUpdate
    private void onUpdate() {
        if (this.updatedBy == null) {
            this.updatedBy = SecurityUtils.getAuthentication();
            this.updatedAt = Timestamp.from(Instant.now());
        }
    }
}
