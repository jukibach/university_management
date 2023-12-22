package fpt.com.universitymanagement.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @NotBlank(message = "User name must not be null")
    private String userName;
    @NotBlank(message = "Password must not be null")
    private String password;
}
