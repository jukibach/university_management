package fpt.com.universitymanagement.service;

import fpt.com.universitymanagement.dto.AccountResponse;
import fpt.com.universitymanagement.dto.ActivationRequest;
import fpt.com.universitymanagement.dto.JwtResponse;
import fpt.com.universitymanagement.dto.LoginRequest;
import java.util.List;

public interface AccountService {
    List<AccountResponse> getAllAccounts();
    
    JwtResponse authenticateUser(LoginRequest loginRequest);
    
    AccountResponse switchAccountStatus(ActivationRequest request);
}
