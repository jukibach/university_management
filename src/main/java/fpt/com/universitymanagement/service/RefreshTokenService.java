package fpt.com.universitymanagement.service;

import fpt.com.universitymanagement.dto.TokenRefreshRequest;
import fpt.com.universitymanagement.dto.TokenRefreshResponse;
import fpt.com.universitymanagement.entity.RefreshToken;

import java.util.Optional;
public interface RefreshTokenService {
    Optional<RefreshToken> findByToken(String token);
    RefreshToken createRefreshToken(long accountId, String userName);
    RefreshToken verifyExpiration(RefreshToken token);
    TokenRefreshResponse refreshToken(TokenRefreshRequest request);
    void deleteToken(RefreshToken token);
    
}
