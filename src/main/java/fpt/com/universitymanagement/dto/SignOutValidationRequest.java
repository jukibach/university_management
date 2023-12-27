package fpt.com.universitymanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignOutValidationRequest {
    @NotBlank(message = "accessToken must not be null")
    private String accessToken;
    @NotNull(message = "accountId must not be null")
    private long accountId;
    @NotNull(message = "isSignOut must not be null")
    private boolean signOut;
}
