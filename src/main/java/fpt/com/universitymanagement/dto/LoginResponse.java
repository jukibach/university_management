package fpt.com.universitymanagement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse {
    private long accountId;
    private String accessToken;
    private String type = "Bearer";
    private String message;
    
    public LoginResponse(long accountId, String accessToken, String message) {
        this.accountId = accountId;
        this.accessToken = accessToken;
        this.message = message;
    }
}
