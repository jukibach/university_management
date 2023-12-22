package fpt.com.universitymanagement.service.impl;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import fpt.com.universitymanagement.config.JwtUtils;
import fpt.com.universitymanagement.dto.TokenRefreshRequest;
import fpt.com.universitymanagement.dto.TokenRefreshResponse;
import fpt.com.universitymanagement.entity.Account;
import fpt.com.universitymanagement.entity.RefreshToken;
import fpt.com.universitymanagement.exception.TokenRefreshException;
import fpt.com.universitymanagement.repository.AccountRepository;
import fpt.com.universitymanagement.repository.RefreshTokenRepository;
import fpt.com.universitymanagement.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    @Value("${app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;
    private final JwtUtils jwtUtils;
    private final RefreshTokenRepository refreshTokenRepository;
    
    private final AccountRepository repository;
    
    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository, AccountRepository repository, JwtUtils jwtUtils) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.repository = repository;
        this.jwtUtils = jwtUtils;
    }
    
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }
    
    @Override
    @Transactional
    public RefreshToken createRefreshToken(UUID accountId, String userName) {
        RefreshToken refreshToken = new RefreshToken();
        Optional<Account> account = repository.findById(accountId);
        if (account.isEmpty()) {
            return null;
        }
        deleteByAccountId(accountId);
        refreshToken.setAccount(account.get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());
        LocalDate currentDate = LocalDate.now(); // Current date based on the system clock and default time-zone
        refreshToken.setCreatedAt(currentDate);
        refreshToken.setCreatedBy(userName);
        
        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }
    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new sign in request");
        }
        return token;
    }
    
    public void deleteByAccountId(UUID accountId) {
        Optional<Account> account = repository.findById(accountId);
        if (account.isEmpty()) {
            return;
        }
        refreshTokenRepository.deleteByAccount(account.get());
    }
    
    @Override
    public TokenRefreshResponse refreshToken(TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        return findByToken(requestRefreshToken)
                .map(this::verifyExpiration)
                .map(RefreshToken::getAccount)
                .map(account -> {
                    String token = jwtUtils.generateTokenFromUsername(account.getUserName());
                    return new TokenRefreshResponse(token, requestRefreshToken);
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }
}