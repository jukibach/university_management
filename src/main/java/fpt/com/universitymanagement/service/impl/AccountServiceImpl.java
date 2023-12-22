package fpt.com.universitymanagement.service.impl;

import fpt.com.universitymanagement.config.JwtUtils;
import fpt.com.universitymanagement.dto.AccountResponse;
import fpt.com.universitymanagement.dto.JwtResponse;
import fpt.com.universitymanagement.dto.LoginRequest;
import fpt.com.universitymanagement.entity.Account;
import fpt.com.universitymanagement.entity.RefreshToken;
import fpt.com.universitymanagement.repository.AccountRepository;
import fpt.com.universitymanagement.service.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
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
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        RefreshToken refreshToken = refreshTokenServiceImpl.createRefreshToken(userDetails.getId(), loginRequest.getUserName());
        
        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles,
                refreshToken.getToken());
    }
}
