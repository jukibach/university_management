package fpt.com.universitymanagement.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class JwtResponse {
    private String token;
    private String tokenExpiration;
    private UUID id;
    private String userName;
    private String email;
    private List<String> role;
    private String type = "Bearer";
    private String refreshToken;
    
    public JwtResponse(String token, String tokenExpiration, UUID id, String userName, String email, List<String> role, String refreshToken) {
        this.token = token;
        this.tokenExpiration = tokenExpiration;
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.role = role;
        this.refreshToken = refreshToken;
    }
}
