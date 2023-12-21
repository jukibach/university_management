package fpt.com.universitymanagement.controller;

import fpt.com.universitymanagement.entity.Account;
import fpt.com.universitymanagement.service.AccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static fpt.com.universitymanagement.common.Constant.ACCOUNT_CONTROLLER;

@RestController
@RequestMapping(ACCOUNT_CONTROLLER)
public class AccountController {
    private final AccountService service;
    
    public AccountController(AccountService service) {
        this.service = service;
    }
    
    @GetMapping("hello-world")
    public String helloWorld() {
        return "Hello";
    }
    
    @GetMapping
    public List<Account> getAllAccounts() {
        return service.getAllAccounts();
    }
}
