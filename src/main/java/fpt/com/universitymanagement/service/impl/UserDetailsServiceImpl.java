package fpt.com.universitymanagement.service.impl;

import fpt.com.universitymanagement.entity.Account;
import fpt.com.universitymanagement.exception.InactiveAccountException;
import fpt.com.universitymanagement.repository.AccountRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AccountRepository repository;
    
    public UserDetailsServiceImpl(AccountRepository repository) {
        this.repository = repository;
    }
    
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = repository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        if (!account.isActivated()) {
            throw new InactiveAccountException("User is not activated");
        }
        return UserDetailsImpl.build(account);
    }
    
}