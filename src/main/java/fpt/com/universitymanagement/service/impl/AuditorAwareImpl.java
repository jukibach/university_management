package fpt.com.universitymanagement.service.impl;

import fpt.com.universitymanagement.config.SecurityUtils;
import lombok.NonNull;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {
    
    @Override
    @NonNull
    public Optional<String> getCurrentAuditor() {
        return Optional.of(SecurityUtils.getAuthentication());
    }
}
