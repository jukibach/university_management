package fpt.com.universitymanagement.service;

import fpt.com.universitymanagement.dto.AccountResponse;
import fpt.com.universitymanagement.dto.ActivationRequest;
import fpt.com.universitymanagement.dto.LoginRequest;
import fpt.com.universitymanagement.dto.LoginResponse;
import fpt.com.universitymanagement.dto.RefreshTokenRequest;
import fpt.com.universitymanagement.dto.SignOutValidationRequest;
import fpt.com.universitymanagement.dto.RefreshTokenResponse;
import fpt.com.universitymanagement.dto.UpdateAccountRequest;
import fpt.com.universitymanagement.entity.account.LoginHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface AccountService {
    Page<AccountResponse> getAllAccounts(Pageable pageable, String searchInput);
    
    LoginResponse login(LoginRequest loginRequest, String ipAddress);
    
    Map<String, String> signOutValidation(SignOutValidationRequest signOutValidationRequest);
    
    void logout(String accessToken);
    
    RefreshTokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
    
    AccountResponse switchAccountStatus(ActivationRequest request);
    
    LoginHistory findByToken(String accessToken);
    
    AccountResponse displayAccountInfo(long accountId);
    
    AccountResponse updateAccountInfo(long accountId, UpdateAccountRequest request);
    
}
