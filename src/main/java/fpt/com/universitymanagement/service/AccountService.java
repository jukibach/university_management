package fpt.com.universitymanagement.service;

import fpt.com.universitymanagement.dto.AccountResponse;
import fpt.com.universitymanagement.dto.ActivationRequest;
import fpt.com.universitymanagement.dto.LoginResponse;
import fpt.com.universitymanagement.dto.LoginRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AccountService {
    List<AccountResponse> getAllAccounts(Pageable pageable, String searchInput);
    
    LoginResponse authenticateUser(LoginRequest loginRequest);
    
    AccountResponse switchAccountStatus(ActivationRequest request);
}
