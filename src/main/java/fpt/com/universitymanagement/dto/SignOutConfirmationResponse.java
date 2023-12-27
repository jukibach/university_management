package fpt.com.universitymanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class SignOutConfirmationResponse {
    private String accessToken;
    private String message;
    private Date timestamp;
    private String type = "Bearer";
    
    public SignOutConfirmationResponse(String accessToken, String message, Date timestamp) {
        this.accessToken = accessToken;
        this.message = message;
        this.timestamp = timestamp;
    }
}
