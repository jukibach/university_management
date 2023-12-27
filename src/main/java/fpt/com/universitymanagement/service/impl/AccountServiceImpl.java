package fpt.com.universitymanagement.service.impl;

import fpt.com.universitymanagement.config.JwtUtils;
import fpt.com.universitymanagement.dto.AccountResponse;
import fpt.com.universitymanagement.dto.ActivationRequest;
import fpt.com.universitymanagement.dto.LoginResponse;
import fpt.com.universitymanagement.dto.LoginRequest;
import fpt.com.universitymanagement.entity.account.Account;
import fpt.com.universitymanagement.entity.account.RefreshToken;
import fpt.com.universitymanagement.repository.AccountRepository;
import fpt.com.universitymanagement.service.AccountService;
import fpt.com.universitymanagement.specification.AccountSpecification;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    private final RefreshTokenServiceImpl refreshTokenServiceImpl;
    
    public AccountServiceImpl(AccountRepository accountRepository, AuthenticationManager authenticationManager, JwtUtils jwtUtils, RefreshTokenServiceImpl refreshTokenServiceImpl) {
        this.accountRepository = accountRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.refreshTokenServiceImpl = refreshTokenServiceImpl;
    }
    
    @Override
    public Page<AccountResponse> getAllAccounts(Pageable pageable, String searchInput) {
        AccountSpecification accountSpecification = new AccountSpecification(searchInput);
        Page<Account> accounts =  accountRepository.findAll(accountSpecification, pageable);
        return accounts.map(this::convertToDto);
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
    public LoginResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        RefreshToken refreshToken = refreshTokenServiceImpl.createRefreshToken(userDetails.getId(), loginRequest.getUserName());
        return new LoginResponse(jwt,
                refreshToken.getToken());
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
    
    @Override
    public void logout(HttpServletRequest request) {
    
    }
}
