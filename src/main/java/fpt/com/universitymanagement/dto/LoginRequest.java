package fpt.com.universitymanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @NotBlank(message = "User name must not be null")
    @Size(message = "User name must have at least 5 characters", min = 4)
    private String userName;
    
    @NotBlank(message = "Password must not be null")
    @Size(message = "Password must have at least 5 characters", min = 4)
    private String password;
}
