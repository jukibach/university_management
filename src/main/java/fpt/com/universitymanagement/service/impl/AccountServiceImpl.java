package fpt.com.universitymanagement.service.impl;

import fpt.com.universitymanagement.common.JwtUtils;
import fpt.com.universitymanagement.dto.AccountResponse;
import fpt.com.universitymanagement.dto.ActivationRequest;
import fpt.com.universitymanagement.dto.LoginRequest;
import fpt.com.universitymanagement.dto.LoginResponse;
import fpt.com.universitymanagement.dto.RefreshTokenRequest;
import fpt.com.universitymanagement.dto.SignOutValidationRequest;
import fpt.com.universitymanagement.dto.TokenRefreshResponse;
import fpt.com.universitymanagement.entity.account.LoginHistory;
import fpt.com.universitymanagement.entity.account.Account;
import fpt.com.universitymanagement.exception.InactiveAccountException;
import fpt.com.universitymanagement.exception.NotFoundException;
import fpt.com.universitymanagement.mapper.AccountMapper;
import fpt.com.universitymanagement.repository.LoginHistoryRepository;
import fpt.com.universitymanagement.repository.AccountRepository;
import fpt.com.universitymanagement.service.AccountService;
import fpt.com.universitymanagement.specification.AccountSpecification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static fpt.com.universitymanagement.common.Constant.FAIL;
import static fpt.com.universitymanagement.common.Constant.LIMITED_ATTEMPTS;
import static fpt.com.universitymanagement.common.Constant.MILLISECONDS;
import static fpt.com.universitymanagement.common.Constant.SUCCESS;


@Service
public class AccountServiceImpl implements AccountService {
    private final JwtUtils jwtUtils;
    private final AccountRepository accountRepository;
    private final LoginHistoryRepository loginHistoryRepository;
    @Value("${app.jwtExpirationMs}")
    private long jwtExpirationMs;
    private final AccountMapper accountMapper;
    
    public AccountServiceImpl(AccountRepository accountRepository, JwtUtils jwtUtils, LoginHistoryRepository loginHistoryRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.jwtUtils = jwtUtils;
        this.loginHistoryRepository = loginHistoryRepository;
        this.accountMapper = accountMapper;
    }
    
    @Override
    public Page<AccountResponse> getAllAccounts(Pageable pageable, String searchInput) {
        AccountSpecification accountSpecification = new AccountSpecification(searchInput);
        Page<Account> accounts = accountRepository.findAll(accountSpecification, pageable);
        return accounts.map(accountMapper::accountToAccountResponse);
    }
    
    @Override
    public LoginResponse login(LoginRequest loginRequest, String ipAddress) {
        Account account = findByUserName(loginRequest.getUserName());
        checkIfAccountLocked(account);
        checkIfPasswordIncorrect(loginRequest, ipAddress, account);
        boolean tokenExists = !account.getLoginHistories().isEmpty();
        LoginHistory loginHistory = createNewLoginHistory(account, ipAddress);
        loginHistoryRepository.save(loginHistory);
        loginHistoryRepository.deleteByStatusAndAccountId(account.getId());
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAccessToken(loginHistory.getToken());
        if (tokenExists) {
            loginResponse.setAnotherTokensExists(true);
        }
        return loginResponse;
    }
    
    @Override
    public AccountResponse switchAccountStatus(ActivationRequest request) {
        Account account = findByUserName(request.getUserName());
        account.setActivated(request.isActivated());
        account = accountRepository.save(account);
        if (request.isActivated()) {
            loginHistoryRepository.deleteByStatusAndAccountId(account.getId());
        }
        return accountMapper.accountToAccountResponse(account);
    }
    
    @Override
    public Map<String, String> signOutValidation(SignOutValidationRequest signOutValidationRequest) {
        Map<String, String> signOutValidationResponse = new HashMap<>();
        List<LoginHistory> loginHistoryList;
        if (signOutValidationRequest.isSignOut()) {
            loginHistoryList = loginHistoryRepository.findByAccount_Id(signOutValidationRequest.getAccountId());
            loginHistoryList = loginHistoryList.stream().filter(s -> !Objects.equals(s.getToken(), signOutValidationRequest.getAccessToken())).toList();
            signOutValidationResponse.put("message", "Deleted all tokens except the latest token");
        } else {
            loginHistoryList = loginHistoryRepository.findByExpiryDateBeforeAndAccount_Id(LocalDateTime.now(), signOutValidationRequest.getAccountId());
            signOutValidationResponse.put("message", "Deleted all expired tokens");
        }
        loginHistoryRepository.deleteAll(loginHistoryList);
        return signOutValidationResponse;
    }
    
    @Override
    public void logout(String accessToken) {
        loginHistoryRepository.delete(findByToken(accessToken));
    }
    
    @Override
    public LoginHistory findByToken(String accessToken) {
        return loginHistoryRepository.findByToken(accessToken).orElseThrow(() -> new NotFoundException("Access token does not exist"));
    }
    
    @Override
    @Transactional
    public TokenRefreshResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        LoginHistory loginHistory = findByToken(refreshTokenRequest.getAccessToken());
        TokenRefreshResponse tokenRefreshResponse = new TokenRefreshResponse();
        String newAccessToken = jwtUtils.generateJwtToken(loginHistory.getAccount().getUserName());
        loginHistory.setToken(newAccessToken);
        loginHistory.setExpiryDate(LocalDateTime.now().plusHours(jwtExpirationMs / MILLISECONDS));
        loginHistoryRepository.save(loginHistory);
        tokenRefreshResponse.setAccessToken(newAccessToken);
        return tokenRefreshResponse;
    }
    
    private Account findByUserName(String userName) {
        return accountRepository.findByUserName(userName).orElseThrow(() -> new UsernameNotFoundException("Username does not exist"));
    }
    
    private LoginHistory createNewLoginHistory(Account account, String ipAddress) {
        String jwt = jwtUtils.generateJwtToken(account.getUserName());
        LoginHistory loginHistory = new LoginHistory();
        loginHistory.setToken(jwt);
        loginHistory.setStatus(SUCCESS);
        loginHistory.setIpAddress(ipAddress);
        loginHistory.setExpiryDate(LocalDateTime.now().plusHours(jwtExpirationMs / MILLISECONDS));
        loginHistory.setCreatedBy(account.getUserName());
        loginHistory.setAccount(account);
        return loginHistory;
    }
    
    private boolean hasConsecutiveFailures(List<LoginHistory> loginHistories) {
        int consecutiveFailures = 0;
        for (LoginHistory history : loginHistories) {
            if (history.getStatus().equals(FAIL)) {
                consecutiveFailures++;
                if (consecutiveFailures == LIMITED_ATTEMPTS) {
                    return true;
                }
            } else {
                consecutiveFailures = 0;
            }
        }
        return false;
    }
    
    private LoginHistory createFailedLoginHistory(String ipAddress, Account account) {
        LoginHistory loginHistory;
        loginHistory = new LoginHistory();
        loginHistory.setStatus(FAIL);
        loginHistory.setIpAddress(ipAddress);
        loginHistory.setCreatedBy(account.getUserName());
        loginHistory.setAccount(account);
        return loginHistory;
    }
    
    private void checkIfPasswordIncorrect(LoginRequest loginRequest, String ipAddress, Account account) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(loginRequest.getPassword(), account.getPassword())) {
            LoginHistory loginHistory = createFailedLoginHistory(ipAddress, account);
            loginHistoryRepository.save(loginHistory);
            throw new UsernameNotFoundException("Incorrect password");
        }
    }
    
    private void checkIfAccountLocked(Account account) {
        List<LoginHistory> failedAttempts = loginHistoryRepository.findByAccount_IdOrderByCreatedAtDesc(
                account.getId(), PageRequest.of(0, LIMITED_ATTEMPTS));
        if (hasConsecutiveFailures(failedAttempts)) {
            account.setActivated(false);
            account.setUpdatedBy(account.getUserName());
            accountRepository.save(account);
            throw new InactiveAccountException("Account is deactivated");
        }
    }
}
