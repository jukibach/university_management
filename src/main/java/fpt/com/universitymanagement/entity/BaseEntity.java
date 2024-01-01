package fpt.com.universitymanagement.entity;

import fpt.com.universitymanagement.common.SecurityUtils;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

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
        if (!StringUtils.hasText(this.createdBy)) {
            this.createdBy = SecurityUtils.getAuthentication();
        }
        this.createdAt = Timestamp.from(Instant.now());
    }
    
    @PreUpdate
    private void onUpdate() {
        if (!StringUtils.hasText(this.updatedBy)) {
            this.updatedBy = SecurityUtils.getAuthentication();
        }
        if (ObjectUtils.isEmpty(this.updatedAt)) {
            this.updatedAt = Timestamp.from(Instant.now());
        }
    }
}
