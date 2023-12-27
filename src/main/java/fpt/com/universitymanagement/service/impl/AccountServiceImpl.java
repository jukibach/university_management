package fpt.com.universitymanagement.service.impl;

import fpt.com.universitymanagement.config.JwtUtils;
import fpt.com.universitymanagement.dto.*;
import fpt.com.universitymanagement.entity.account.AccessToken;
import fpt.com.universitymanagement.entity.account.Account;
import fpt.com.universitymanagement.repository.AccessTokenRepository;
import fpt.com.universitymanagement.repository.AccountRepository;
import fpt.com.universitymanagement.service.AccountService;
import fpt.com.universitymanagement.specification.AccountSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static fpt.com.universitymanagement.common.Constant.MILLISECONDS;


@Service
public class AccountServiceImpl implements AccountService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final AccountRepository accountRepository;
    private final AccessTokenRepository accessTokenRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    @Value("${app.jwtExpirationMs}")
    private long jwtExpirationMs;
    
    public AccountServiceImpl(AccountRepository accountRepository, AuthenticationManager authenticationManager, JwtUtils jwtUtils, AccessTokenRepository accessTokenRepository) {
        this.accountRepository = accountRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.accessTokenRepository = accessTokenRepository;
    }
    
    @Override
    public Page<AccountResponse> getAllAccounts(Pageable pageable, String searchInput) {
        AccountSpecification accountSpecification = new AccountSpecification(searchInput);
        Page<Account> accounts = accountRepository.findAll(accountSpecification, pageable);
        return accounts.map(this::convertToDto);
    }
    
    @Override
    public LoginResponse authenticateUser(LoginRequest loginRequest) {
        LoginResponse loginResponse = new LoginResponse();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));
        String jwt = jwtUtils.generateJwtToken(authentication);
        AccessToken accessToken = new AccessToken();
        accessToken.setToken(jwt);
        accessToken.setExpiryDate(LocalDateTime.now().plusHours(jwtExpirationMs / MILLISECONDS));
        accessToken.setCreatedBy(loginRequest.getUserName());
        accessToken.setAccount(accountRepository.findByUserName(loginRequest.getUserName()).orElse(null));
        accessTokenRepository.save(accessToken);
        loginResponse.setAccessToken(jwt);
        if (loginRequest.isValidated()) {
            boolean tokenExists = accessTokenRepository.existsByAccount_UserName(loginRequest.getUserName());
            if (tokenExists) {
                loginResponse.setAnotherTokensExists(true);
                return loginResponse;
            }
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
        return account.map(this::convertToDto).orElse(null);
    }
    
    @Override
    public SignOutValidationResponse signOutValidation(SignOutValidationRequest signOutValidationRequest) {
        SignOutValidationResponse signOutValidationResponse = new SignOutValidationResponse();
        List<AccessToken> accessTokenList;
        if (signOutValidationRequest.isSignOut()) {
            accessTokenList = accessTokenRepository.findByAccount_Id(signOutValidationRequest.getAccountId());
            accessTokenList = accessTokenList.stream().filter(s -> !Objects.equals(s.getToken(), signOutValidationRequest.getAccessToken())).toList();
            accessTokenRepository.deleteAll(accessTokenList);
            signOutValidationResponse.setMessage("Deleted all tokens except the latest token");
        } else {
            accessTokenList = accessTokenRepository.findByExpiryDateBeforeAndAccount_Id(LocalDateTime.now(), signOutValidationRequest.getAccountId());
            accessTokenRepository.deleteAll(accessTokenList);
            signOutValidationResponse.setMessage("Deleted all expired tokens");
        }
        return signOutValidationResponse;
    }
    
    public AccountResponse convertToDto(Account account) {
        AccountResponse dto = new AccountResponse();
        modelMapper.typeMap(Account.class, AccountResponse.class).addMappings(mapper ->
                mapper.skip(AccountResponse::setRoleAccounts)
        );
        modelMapper.map(account, dto);
        Set<String> roleDTOs = account.getRoleAccounts().stream()
                .map(roleAccount -> roleAccount.getRole().getName())
                .collect(Collectors.toSet());
        dto.setRoleAccounts(roleDTOs);
        return dto;
    }
}
