package fpt.com.universitymanagement.controller;

import fpt.com.universitymanagement.dto.*;
import fpt.com.universitymanagement.exception.ErrorMessage;
import fpt.com.universitymanagement.service.AccountService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.local.LocalBucketBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static fpt.com.universitymanagement.common.Constant.*;

@RestController
@RequestMapping(AUTH_CONTROLLER)
public class AuthController {
    private final AccountService accountService;
    
    public AuthController(AccountService accountService) {
        this.accountService = accountService;
    }
    
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();
    
    @Operation(summary = "Sign in by using username and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Signed in successfully!", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))
            }),
            @ApiResponse(responseCode = "429", description = "Too many requests!", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
            })})
    @PostMapping("/login")
    public ResponseEntity<Object> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, WebRequest request) {
        String key = loginRequest.getUserName();
        Bucket bucket = cache.computeIfAbsent(key, k -> createNewBucket());
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        if (probe.isConsumed()) {
            LoginResponse loginResponse = accountService.login(loginRequest);
            if (Objects.equals(loginResponse.getMessage(), "Incorrect password")) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new LoginResponse(
                                null,
                                loginResponse.getMessage(),
                                false
                        ));
            }
            cache.remove(key);
            if (loginResponse.isAnotherTokensExists()) {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(new LoginResponse(
                                loginResponse.getAccessToken(),
                                "There are login sessions in another devices. Do you want to keep this session ?",
                                true
                        ));
            }
            return ResponseEntity.ok(loginResponse);
        } else {
            // Too many requests
            long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(new ErrorMessage(
                            HttpStatus.TOO_MANY_REQUESTS.value(),
                            new Date(),
                            "You have exhausted your API request quota. Try again in " + waitForRefill + " seconds.",
                            request.getDescription(false)
                    ));
        }
    }
    
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logged out successfully!", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            })})
    @Operation(summary = "Log out for a login session")
    @PostMapping("/logout")
    public ResponseEntity<Object> signOut(@RequestBody String accessToken) {
        accountService.logout(accessToken);
        return ResponseEntity.ok("Logout successfully");
    }
    
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Signed out successfully!", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            })})
    @Operation(summary = "Sign out all login sessions except the latest login session")
    @PostMapping("/sign-out-validation")
    public ResponseEntity<Object> validateSignOut(@Valid @RequestBody SignOutValidationRequest signOutValidationRequest) {
        Map<String, String> signOutValidationResponse = accountService.signOutValidation(signOutValidationRequest);
        return new ResponseEntity<>(signOutValidationResponse, HttpStatus.OK);
    }
    
    @Operation(summary = "Refresh token for an account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Refreshed successfully!", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TokenRefreshResponse.class))
            })})
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/refresh-token")
    public ResponseEntity<Object> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(accountService.refreshToken(refreshTokenRequest));
    }
    
    private Bucket createNewBucket() {
        return new LocalBucketBuilder()
                .addLimit(Bandwidth.simple(LIMITED_ATTEMPTS, Duration.ofHours(LIMITED_DURATION))) // Example: 5 requests per hour
                .build();
    }
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}