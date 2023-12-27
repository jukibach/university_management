package fpt.com.universitymanagement.dto;

import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;

@Getter
@Setter
public class BaseResponse {
    private Timestamp createdAt;
    
    private String createdBy;
    
    private Timestamp updatedAt;
    
    private String updatedBy;
}
