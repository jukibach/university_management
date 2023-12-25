package fpt.com.universitymanagement.controller;

import fpt.com.universitymanagement.dto.AccountResponse;
import fpt.com.universitymanagement.service.AccountService;
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
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<AccountResponse> getAllAccounts() {
        return service.getAllAccounts();
    }
}
