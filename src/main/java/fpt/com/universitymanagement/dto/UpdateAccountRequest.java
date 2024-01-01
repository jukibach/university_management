package fpt.com.universitymanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UpdateAccountRequest {
    @NotBlank(message = "User name must not be null")
    private String userName;
    @NotNull(message = "Roles must not be null")
    private Set<String> roleAccounts;
}
