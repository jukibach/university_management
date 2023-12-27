package fpt.com.universitymanagement.exception;

import lombok.Getter;

@Getter
public class ConflictedLoginSessionException extends RuntimeException {
    private final String token;
    private final long accountId;
    
    public ConflictedLoginSessionException(long accountId, String msg, String token) {
        super(msg);
        this.token = token;
        this.accountId = accountId;
    }
}
