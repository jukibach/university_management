package fpt.com.universitymanagement.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TokenRefreshResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    
    public TokenRefreshResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
