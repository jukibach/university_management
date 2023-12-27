package fpt.com.universitymanagement.controller;

import fpt.com.universitymanagement.dto.AccountResponse;
import fpt.com.universitymanagement.dto.ActivationRequest;
import fpt.com.universitymanagement.dto.UpdateAccountRequest;
import fpt.com.universitymanagement.service.AccountService;
import fpt.com.universitymanagement.service.AzureStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static fpt.com.universitymanagement.common.Constant.ACCOUNT_CONTROLLER;

@RestController
@RequestMapping(ACCOUNT_CONTROLLER)
public class AccountController {
    private final AccountService accountService;
    private final AzureStorageService azureStorageService;
    
    public AccountController(AccountService accountService, AzureStorageService azureStorageService) {
        this.accountService = accountService;
        this.azureStorageService = azureStorageService;
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
    @ResponseStatus(HttpStatus.OK)
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
    @PutMapping("/active")
    @ResponseStatus(HttpStatus.OK)
    public AccountResponse switchAccountStatus(@RequestBody ActivationRequest request) {
        return accountService.switchAccountStatus(request);
    }
    
    @Operation(summary = "Display personal profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetched successfully!", content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = AccountResponse.class)))
            })
    })
    @GetMapping("/personal-profile/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER' ,'STUDENT', 'INSTRUCTOR')")
    @SecurityRequirement(name = "Bearer Authentication")
    @ResponseStatus(HttpStatus.OK)
    public AccountResponse displayAccountInfo(@PathVariable("id") long accountId) {
        return accountService.displayAccountInfo(accountId);
    }
    
    @Operation(summary = "Update personal profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated successfully!", content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = AccountResponse.class)))
            })
    })
    @PatchMapping("/personal-profile/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER' ,'STUDENT', 'INSTRUCTOR')")
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "Bearer Authentication")
    public AccountResponse updateAccountInfo(@PathVariable("id") long accountId, @Valid @RequestBody UpdateAccountRequest accountRequest) {
        return accountService.updateAccountInfo(accountId, accountRequest);
    }
    
    @Operation(summary = "Upload files on Azure")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uploaded successfully!", content = {
                    @Content(mediaType = "text/plain",
                            array = @ArraySchema(schema = @Schema(implementation = String.class)))
            })
    })
    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "Bearer Authentication")
    public String uploadFiles(@RequestParam("file") MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        return azureStorageService.uploadFile(file.getInputStream(), fileName);  // Return the URL of the uploaded image
    }
}
