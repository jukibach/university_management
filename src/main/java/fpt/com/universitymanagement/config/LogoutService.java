package fpt.com.universitymanagement.config;

import fpt.com.universitymanagement.entity.account.RefreshToken;
import fpt.com.universitymanagement.repository.RefreshTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
public class LogoutService implements LogoutHandler {
    private final RefreshTokenRepository refreshTokenRepository;
    
    public LogoutService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }
    
    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        jwt = authHeader.substring(7);
        RefreshToken storedToken = refreshTokenRepository.findByToken(jwt)
                .orElse(null);
        if (storedToken != null) {
            refreshTokenRepository.delete(storedToken);
            SecurityContextHolder.clearContext();
        }
    }
}
