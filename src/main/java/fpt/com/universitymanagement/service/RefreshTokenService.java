package fpt.com.universitymanagement.service;

import fpt.com.universitymanagement.dto.TokenRefreshRequest;
import fpt.com.universitymanagement.dto.TokenRefreshResponse;
import fpt.com.universitymanagement.entity.RefreshToken;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenService {
    Optional<RefreshToken> findByToken(String token);
    
    RefreshToken createRefreshToken(UUID accountId, String userName);
    
    RefreshToken verifyExpiration(RefreshToken token);
    TokenRefreshResponse refreshToken(TokenRefreshRequest request);
}
