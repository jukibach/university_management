package fpt.com.universitymanagement.service;

import fpt.com.universitymanagement.dto.AccountResponse;
import fpt.com.universitymanagement.dto.ActivationRequest;
import fpt.com.universitymanagement.dto.LoginResponse;
import fpt.com.universitymanagement.dto.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccountService {
    Page<AccountResponse> getAllAccounts(Pageable pageable, String searchInput);
    
    LoginResponse authenticateUser(LoginRequest loginRequest);
    
    void logout(HttpServletRequest request);
    
    AccountResponse switchAccountStatus(ActivationRequest request);
}
