package fpt.com.universitymanagement.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @NotBlank(message = "User name must not be null")
    @Min(message = "User name must have at least 8 characters", value = 8)
    private String userName;
    
    @NotBlank(message = "Password must not be null")
    @Min(message = "Password must have at least 8 characters", value = 8)
    private String password;
}
