package fpt.com.universitymanagement.controller;

import fpt.com.universitymanagement.dto.LoginRequest;
import fpt.com.universitymanagement.dto.LoginResponse;
import fpt.com.universitymanagement.dto.RefreshTokenRequest;
import fpt.com.universitymanagement.dto.SignOutValidationRequest;
import fpt.com.universitymanagement.dto.RefreshTokenResponse;
import fpt.com.universitymanagement.exception.ConflictedLoginSessionException;
import fpt.com.universitymanagement.exception.ErrorMessage;
import fpt.com.universitymanagement.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static fpt.com.universitymanagement.common.Constant.*;

@RestController
@RequestMapping(AUTH_CONTROLLER)
public class AuthController {
    private final AccountService accountService;
    
    public AuthController(AccountService accountService) {
        this.accountService = accountService;
    }
    
    
    @Operation(summary = "Sign in by using username and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Signed in successfully!", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))
            }),
            @ApiResponse(responseCode = "429", description = "Too many requests!", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
            })})
    @PostMapping("/login")
    public ResponseEntity<Object> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        LoginResponse loginResponse = accountService.login(loginRequest, ipAddress);
        if (loginResponse.isAnotherTokensExists()) {
            throw new ConflictedLoginSessionException(loginResponse.getAccountId(), "There are login sessions in another devices. Do you want to keep this session ?", loginResponse.getAccessToken());
        }
        return ResponseEntity.ok(loginResponse);
    }
    
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logged out successfully!", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            })})
    @Operation(summary = "Log out for a login session")
    @PostMapping("/logout")
    public ResponseEntity<Object> signOut(@RequestBody String accessToken) {
        accountService.logout(accessToken);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logout successfully");
        return ResponseEntity.ok(response);
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
                    @Content(mediaType = "application/json", schema = @Schema(implementation = RefreshTokenResponse.class))
            })})
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/refresh-token")
    public ResponseEntity<Object> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(accountService.refreshToken(refreshTokenRequest));
    }
}