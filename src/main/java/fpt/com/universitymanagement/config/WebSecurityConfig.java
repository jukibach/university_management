package fpt.com.universitymanagement.config;

import fpt.com.universitymanagement.common.JwtUtils;
import fpt.com.universitymanagement.service.AccountService;
import fpt.com.universitymanagement.service.impl.AuditorAwareImpl;
import fpt.com.universitymanagement.service.impl.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class WebSecurityConfig {
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtils jwtUtils;
    
    private final AuthEntryPointJwt unauthorizedHandler;
    
    private final CustomAccessDeniedHandler accessDeniedHandler;
    
    private final AccountService accountService;
    
    public WebSecurityConfig(UserDetailsServiceImpl userDetailsService, JwtUtils jwtUtils, AuthEntryPointJwt unauthorizedHandler, CustomAccessDeniedHandler accessDeniedHandler, @Lazy AccountService accountService) {
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
        this.unauthorizedHandler = unauthorizedHandler;
        this.accessDeniedHandler = accessDeniedHandler;
        this.accountService = accountService;
    }
    
    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }
    
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter(userDetailsService, jwtUtils, accountService);
    }
    
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(unauthorizedHandler)
                        .accessDeniedHandler(accessDeniedHandler))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/v1/api/auth/**", "/swagger-ui/**", "/v3/**")
                        .permitAll()
                        .requestMatchers("/v1/api/accounts/**")
                        .authenticated());
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
