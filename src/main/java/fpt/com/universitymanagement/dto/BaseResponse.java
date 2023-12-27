package fpt.com.universitymanagement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse {
    private Timestamp createdAt;
    
    private String createdBy;
    
    private Timestamp updatedAt;
    
    private String updatedBy;
}
