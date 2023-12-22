package fpt.com.universitymanagement.controller;

import fpt.com.universitymanagement.dto.LoginRequest;
import fpt.com.universitymanagement.service.AccountService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.local.LocalBucketBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static fpt.com.universitymanagement.common.Constant.AUTH_CONTROLLER;

@RestController
@RequestMapping(AUTH_CONTROLLER)
public class AuthController {
    private final AccountService service;
    
    public AuthController(AccountService service) {
        this.service = service;
    }
    
    @GetMapping("/google")
    public OAuth2User loginWithGoogle(@AuthenticationPrincipal OAuth2User principal) {
        return principal;
    }
    
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();
    
    @PostMapping("/login")
    public ResponseEntity<Object> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        Bucket bucket = cache.computeIfAbsent(ip, this::createNewBucket);
        
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        if (probe.isConsumed()) {
            return ResponseEntity.ok(service.authenticateUser(loginRequest));
        } else {
            // Too many requests
            long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("You have exhausted your API request quota. Try again in " + waitForRefill + " seconds.");
        }
        
    }
    
    private Bucket createNewBucket(String s) {
        return new LocalBucketBuilder()
                .addLimit(Bandwidth.simple(5, Duration.ofHours(1))) // Example: 5 requests per hour
                .build();
    }
}