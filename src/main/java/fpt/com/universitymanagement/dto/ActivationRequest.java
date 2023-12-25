package fpt.com.universitymanagement.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivationRequest {
    @NotBlank(message = "Username must be non-empty")
    private String userName;
    @NotBlank(message = "Field isActivated must be non-empty")
    private Boolean isActivated;
}
