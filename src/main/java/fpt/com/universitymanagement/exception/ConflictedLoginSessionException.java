package fpt.com.universitymanagement.exception;

import lombok.Getter;

@Getter
public class ConflictedLoginSessionException extends RuntimeException {
    private final String token;
    
    public ConflictedLoginSessionException(String msg, String token) {
        super(msg);
        this.token = token;
    }
}
