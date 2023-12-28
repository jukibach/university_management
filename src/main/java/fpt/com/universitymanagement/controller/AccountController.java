package fpt.com.universitymanagement.controller;

import fpt.com.universitymanagement.dto.AccountResponse;
import fpt.com.universitymanagement.dto.ActivationRequest;
import fpt.com.universitymanagement.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static fpt.com.universitymanagement.common.Constant.ACCOUNT_CONTROLLER;

@RestController
@RequestMapping(ACCOUNT_CONTROLLER)
public class AccountController {
    private final AccountService accountService;
    
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }
    
    @Operation(summary = "Fetch all accounts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetched successfully!", content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Page.class)))
            })
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public Page<AccountResponse> getAllAccounts(Pageable pageable, @Nullable String searchInput) {
        return accountService.getAllAccounts(pageable, searchInput);
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
        return ResponseEntity.ok(accountService.switchAccountStatus(request));
    }
    
    @Operation(summary = "Display personal profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetched successfully!", content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = AccountResponse.class)))
            })
    })
    @GetMapping("/personal-profile")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER' ,'STUDENT', 'INSTRUCTOR')")
    @SecurityRequirement(name = "Bearer Authentication")
    public AccountResponse displayAccountInfo(@RequestParam String accessToken) {
        return accountService.displayAccountInfo(accessToken);
    }
}
