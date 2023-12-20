package fpt.com.universitymanagement.service.implementation;

import fpt.com.universitymanagement.entity.Account;
import fpt.com.universitymanagement.repository.AccountRepository;
import fpt.com.universitymanagement.service.AccountService;
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
