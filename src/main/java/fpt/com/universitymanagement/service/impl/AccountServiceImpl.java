package fpt.com.universitymanagement.service.impl;

import fpt.com.universitymanagement.config.JwtUtils;
import fpt.com.universitymanagement.dto.AccountResponse;
import fpt.com.universitymanagement.dto.ActivationRequest;
import fpt.com.universitymanagement.dto.JwtResponse;
import fpt.com.universitymanagement.dto.LoginRequest;
import fpt.com.universitymanagement.entity.Account;
import fpt.com.universitymanagement.entity.RefreshToken;
import fpt.com.universitymanagement.repository.AccountRepository;
import fpt.com.universitymanagement.service.AccountService;
import fpt.com.universitymanagement.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    private final RefreshTokenService refreshTokenService;
    
    public AccountServiceImpl(AccountRepository accountRepository, AuthenticationManager authenticationManager, JwtUtils jwtUtils, RefreshTokenService refreshTokenService) {
        this.accountRepository = accountRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
    }
    
    @Override
    public List<AccountResponse> getAllAccounts() {
        List<Account> list = accountRepository.findAll();
        return list.stream().map(this::convertToDto).toList();
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
    
    @Override
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Optional<Account> account = accountRepository.findByUserName(loginRequest.getUserName());
        if (account.isEmpty()) {
            return null;
        }
        account.get().setAccessToken(jwt);
        accountRepository.save(account.get());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId(), loginRequest.getUserName());
        return new JwtResponse(jwt,
                refreshToken.getToken());
    }
    
    @Override
    public void logout(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        jwt = authHeader.substring(7);
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        Optional<Account> account = accountRepository.findByUserName(username);
        if (account.isEmpty()) {
            return;
        }
        account.get().setAccessToken(null);
        accountRepository.save(account.get());
        RefreshToken storedToken = refreshTokenService.findByToken(jwt)
                .orElse(null);
        if (storedToken != null) {
            refreshTokenService.deleteToken(storedToken);
            SecurityContextHolder.clearContext();
        }
    }
    
    @Override
    public AccountResponse switchAccountStatus(ActivationRequest request) {
        Optional<Account> account = accountRepository.findByUserName(request.getUserName());
        if (account.isEmpty()) {
            return null;
        }
        account.get().setActivated(request.getIsActivated());
        account = Optional.of(accountRepository.save(account.get()));
        return account.map(this::convertToDto).orElse(null);
    }
}
