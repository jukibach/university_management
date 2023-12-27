package fpt.com.universitymanagement.service;

import fpt.com.universitymanagement.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccountService {
    Page<AccountResponse> getAllAccounts(Pageable pageable, String searchInput);
    
    LoginResponse login(LoginRequest loginRequest);
    
    SignOutValidationResponse signOutValidation(SignOutValidationRequest signOutValidationRequest);
    
    AccountResponse switchAccountStatus(ActivationRequest request);
}
