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
    private boolean anotherTokensExists = false;
}
