package fpt.com.universitymanagement.service.impl;

import fpt.com.universitymanagement.common.JwtUtils;
import fpt.com.universitymanagement.dto.*;
import fpt.com.universitymanagement.entity.account.AccessToken;
import fpt.com.universitymanagement.entity.account.Account;
import fpt.com.universitymanagement.exception.NotFoundException;
import fpt.com.universitymanagement.mapper.AccountMapper;
import fpt.com.universitymanagement.repository.AccessTokenRepository;
import fpt.com.universitymanagement.repository.AccountRepository;
import fpt.com.universitymanagement.service.AccountService;
import fpt.com.universitymanagement.specification.AccountSpecification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

import static fpt.com.universitymanagement.common.Constant.MILLISECONDS;


@Service
public class AccountServiceImpl implements AccountService {
    private final JwtUtils jwtUtils;
    private final AccountRepository accountRepository;
    private final AccessTokenRepository accessTokenRepository;
    @Value("${app.jwtExpirationMs}")
    private long jwtExpirationMs;
    private final AccountMapper accountMapper;
    
    public AccountServiceImpl(AccountRepository accountRepository, JwtUtils jwtUtils, AccessTokenRepository accessTokenRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.jwtUtils = jwtUtils;
        this.accessTokenRepository = accessTokenRepository;
        this.accountMapper = accountMapper;
    }
    
    @Override
    public Page<AccountResponse> getAllAccounts(Pageable pageable, String searchInput) {
        AccountSpecification accountSpecification = new AccountSpecification(searchInput);
        Page<Account> accounts = accountRepository.findAll(accountSpecification, pageable);
        return accounts.map(accountMapper::accountToAccountResponse);
    }
    
    @Override
    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {
        LoginResponse loginResponse = new LoginResponse();
        
        Optional<Account> account = accountRepository.findByUserName(loginRequest.getUserName());
        if (account.isEmpty()) {
            throw new UsernameNotFoundException("Username does not exist");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(loginRequest.getPassword(), account.get().getPassword())) {
            throw new UsernameNotFoundException("Incorrect password");
        }
        boolean tokenExists = !account.get().getAccessTokens().isEmpty();
        loginResponse.setAccessToken(getJwt(account.get()));
        if (tokenExists) {
            loginResponse.setAnotherTokensExists(true);
        }
        return loginResponse;
    }
    
    @Override
    public AccountResponse switchAccountStatus(ActivationRequest request) {
        Optional<Account> account = accountRepository.findByUserName(request.getUserName());
        if (account.isEmpty()) {
            return null;
        }
        account.get().setActivated(request.isActivated());
        account = Optional.of(accountRepository.save(account.get()));
        return account.map(accountMapper::accountToAccountResponse).orElse(null);
    }
    
    @Override
    public Map<String, String> signOutValidation(SignOutValidationRequest signOutValidationRequest) {
        Map<String, String> signOutValidationResponse = new HashMap<>();
        List<AccessToken> accessTokenList;
        if (signOutValidationRequest.isSignOut()) {
            accessTokenList = accessTokenRepository.findByAccount_Id(signOutValidationRequest.getAccountId());
            accessTokenList = accessTokenList.stream().filter(s -> !Objects.equals(s.getToken(), signOutValidationRequest.getAccessToken())).toList();
            signOutValidationResponse.put("message", "Deleted all tokens except the latest token");
        } else {
            accessTokenList = accessTokenRepository.findByExpiryDateBeforeAndAccount_Id(LocalDateTime.now(), signOutValidationRequest.getAccountId());
            signOutValidationResponse.put("message", "Deleted all expired tokens");
        }
        accessTokenRepository.deleteAll(accessTokenList);
        return signOutValidationResponse;
    }
    
    @Override
    public void logout(String accessToken) {
        accessTokenRepository.delete(findByToken(accessToken));
    }
    
    @Override
    public AccessToken findByToken(String accessToken) {
        return accessTokenRepository.findByToken(accessToken).orElseThrow(() -> new NotFoundException("Access token does not exist"));
    }
    
    private String getJwt(Account account) {
        String jwt = jwtUtils.generateJwtToken(account.getUserName());
        AccessToken accessToken = new AccessToken();
        accessToken.setToken(jwt);
        accessToken.setExpiryDate(LocalDateTime.now().plusHours(jwtExpirationMs / MILLISECONDS));
        accessToken.setCreatedBy(account.getUserName());
        accessToken.setCreatedAt(Timestamp.from(Instant.now()));
        accessToken.setAccount(account);
        accessTokenRepository.save(accessToken);
        return jwt;
    }
}
