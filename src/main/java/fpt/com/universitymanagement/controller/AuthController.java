package fpt.com.universitymanagement.controller;

import fpt.com.universitymanagement.dto.*;
import fpt.com.universitymanagement.exception.ErrorMessage;
import fpt.com.universitymanagement.service.AccountService;
import fpt.com.universitymanagement.service.RefreshTokenService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static fpt.com.universitymanagement.common.Constant.AUTH_CONTROLLER;

@RestController
@RequestMapping(AUTH_CONTROLLER)
public class AuthController {
    private final AccountService service;
    private final RefreshTokenService refreshTokenService;
    
    public AuthController(AccountService service, RefreshTokenService refreshTokenService) {
        this.service = service;
        this.refreshTokenService = refreshTokenService;
    }
    
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();
    
    @Operation(summary = "Sign in by using username and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Signed in successfully!", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = JwtResponse.class))
            }),
            @ApiResponse(responseCode = "429", description = "Too many requests!")})
    @PostMapping("/login")
    public ResponseEntity<Object> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        String key = loginRequest.getUserName();
        Bucket bucket = cache.computeIfAbsent(key, this::createNewBucket);
        
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        if (probe.isConsumed()) {
            JwtResponse jwtResponse = service.authenticateUser(loginRequest);
            cache.remove(key);
            return ResponseEntity.ok(jwtResponse);
        } else {
            // Too many requests
            long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("You have exhausted your API request quota. Try again in " + waitForRefill + " seconds.");
        }
    }
    
    @Operation(summary = "Activate/Deactivate an account")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Changed status successfully!", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AccountResponse.class))
            })})
    @PostMapping("/active")
    public ResponseEntity<AccountResponse> switchAccountStatus(@RequestBody ActivationRequest request) {
        return ResponseEntity.ok(service.switchAccountStatus(request));
    }
    
    @Operation(summary = "Refresh token for an account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Refreshed successfully!", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TokenRefreshResponse.class))
            }),
            @ApiResponse(responseCode = "403", description = "Refresh token is not in database!", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
            })})
    @PostMapping("/refresh-token")
    public ResponseEntity<Object> refreshToken(@Valid @RequestBody TokenRefreshRequest dto, WebRequest request) {
        TokenRefreshResponse token = refreshTokenService.refreshToken(dto);
        if (token != null) {
            return ResponseEntity.ok(token);
        }
        return new ResponseEntity<>(new ErrorMessage(
                HttpStatus.FORBIDDEN.value(),
                new Date(),
                String.format("Failed for [%s]: %s", dto.getRefreshToken(), "Refresh token is not in database!"),
                request.getDescription(false)), HttpStatus.FORBIDDEN);
    }
    
    private Bucket createNewBucket(String s) {
        return new LocalBucketBuilder()
                .addLimit(Bandwidth.simple(5, Duration.ofHours(1))) // Example: 5 requests per hour
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