package fpt.com.universitymanagement.exception;

import org.springframework.security.core.AuthenticationException;

public class InactiveAccountException extends AuthenticationException {
    public InactiveAccountException(String msg) {
        super(msg);
    }
}
