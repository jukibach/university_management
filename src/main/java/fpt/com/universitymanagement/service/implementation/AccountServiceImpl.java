package com.example.university_management.service.implementation;

import com.example.university_management.entity.Account;
import com.example.university_management.repository.AccountRepository;
import com.example.university_management.service.AccountService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    
    AccountRepository accountRepository;
    
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    
    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
}
