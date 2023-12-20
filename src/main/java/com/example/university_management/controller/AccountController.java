package com.example.university_management.controller;

import com.example.university_management.entity.Account;
import com.example.university_management.service.AccountService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/api/accounts")
@CrossOrigin(maxAge = 3600)
public class AccountController {
    final
    AccountService service;
    
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
