package fpt.com.universitymanagement.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class AccountResponse {
    private long id;
    private String userName;
    private String email;
    private boolean isActivated;
    private Set<String> roleAccounts;
}
