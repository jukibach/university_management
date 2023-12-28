package fpt.com.universitymanagement.service;

import fpt.com.universitymanagement.dto.*;
import fpt.com.universitymanagement.entity.account.AccessToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface AccountService {
    Page<AccountResponse> getAllAccounts(Pageable pageable, String searchInput);
    
    LoginResponse login(LoginRequest loginRequest);
    
    Map<String, String> signOutValidation(SignOutValidationRequest signOutValidationRequest);
    
    void logout(String accessToken);
    
    AccountResponse switchAccountStatus(ActivationRequest request);
    
    AccessToken findByToken(String accessToken);
}
