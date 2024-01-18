package fpt.com.universitymanagement.service.impl;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import fpt.com.universitymanagement.config.JwtUtils;
import fpt.com.universitymanagement.dto.TokenRefreshRequest;
import fpt.com.universitymanagement.dto.TokenRefreshResponse;
import fpt.com.universitymanagement.entity.account.Account;
import fpt.com.universitymanagement.entity.account.RefreshToken;
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
    
    private final AccountRepository accountRepository;
    
    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository, AccountRepository accountRepository, JwtUtils jwtUtils) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.accountRepository = accountRepository;
        this.jwtUtils = jwtUtils;
    }
    
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }
    
    @Override
    @Transactional
    public RefreshToken createRefreshToken(long accountId, String userName) {
        RefreshToken refreshToken = new RefreshToken();
        Optional<Account> account = accountRepository.findById(accountId);
        if (account.isEmpty()) {
            return null;
        }
        deleteByAccountId(accountId);
        refreshToken.setAccount(account.get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());
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
                .orElse(null);
    }
    
    @Override
    public void deleteToken(RefreshToken token) {
        refreshTokenRepository.delete(token);
    }
    
    private void deleteByAccountId(long accountId) {
        Optional<Account> account = accountRepository.findById(accountId);
        if (account.isEmpty()) {
            return;
        }
        refreshTokenRepository.deleteByAccount(account.get());
    }
}