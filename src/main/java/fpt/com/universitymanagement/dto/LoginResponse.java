package fpt.com.universitymanagement.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginResponse {
    private String accessToken;
    private String type = "Bearer";
    private String message;
    private boolean anotherTokensExists = false;
    
    public LoginResponse(String accessToken, String message, boolean anotherTokensExists) {
        this.accessToken = accessToken;
        this.message = message;
        this.anotherTokensExists = anotherTokensExists;
    }
}
