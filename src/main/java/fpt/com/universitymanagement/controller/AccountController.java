package fpt.com.universitymanagement.controller;

import fpt.com.universitymanagement.entity.Account;
import fpt.com.universitymanagement.service.AccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/api/accounts")
public class AccountController {
    final
    AccountService service;
    
    //test git
    
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
