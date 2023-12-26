package fpt.com.universitymanagement.service.impl;

import java.io.Serial;
import java.util.Collection;
import java.util.List;

import fpt.com.universitymanagement.entity.Account;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Getter
public class UserDetailsImpl implements UserDetails {
    @Serial
    private static final long serialVersionUID = 1L;
    
    private final long id;
    
    private final String username;
    
    private final String email;
    
    @JsonIgnore
    private final String password;
    
    private final Collection<? extends GrantedAuthority> authorities;
    
    public UserDetailsImpl(long id, String username, String email, String password,
                           Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }
    
    public static UserDetailsImpl build(Account account) {
        List<SimpleGrantedAuthority> authorities = account.getRoleAccounts().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole().getName()))
                .toList();
        
        return new UserDetailsImpl(
                account.getId(),
                account.getUserName(),
                account.getEmail(),
                account.getPassword(),
                authorities);
    }
    
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    public boolean isEnabled() {
        return true;
    }
}