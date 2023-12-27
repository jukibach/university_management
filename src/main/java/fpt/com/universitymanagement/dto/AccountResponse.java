package fpt.com.universitymanagement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class AccountResponse extends BaseResponse {
    private long id;
    private String userName;
    private String email;
    @JsonProperty("isActivated")
    private boolean isActivated;
    private Set<String> roleAccounts;
}
