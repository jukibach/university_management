package fpt.com.universitymanagement.service.impl;

import fpt.com.universitymanagement.entity.account.Account;
import fpt.com.universitymanagement.exception.InactiveAccountException;
import fpt.com.universitymanagement.repository.AccountRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AccountRepository accountRepository;
    
    public UserDetailsServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        if (!account.isActivated()) {
            throw new InactiveAccountException("Account is deactivated");
        }
        return UserDetailsImpl.build(account);
    }
}