package fpt.com.universitymanagement.controller;

import fpt.com.universitymanagement.dto.AccountResponse;
import fpt.com.universitymanagement.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static fpt.com.universitymanagement.common.Constant.ACCOUNT_CONTROLLER;

@RestController
@RequestMapping(ACCOUNT_CONTROLLER)
public class AccountController {
    private final AccountService service;
    
    public AccountController(AccountService service) {
        this.service = service;
    }
    
    @Operation(summary = "Fetch all accounts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetched successfully!", content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = AccountResponse.class)))
            })
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public List<AccountResponse> getAllAccounts(Pageable pageable, String searchInput) {
        return service.getAllAccounts(pageable, searchInput);
    }
}
