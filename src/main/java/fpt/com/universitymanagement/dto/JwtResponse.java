package fpt.com.universitymanagement.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class JwtResponse {
    private String token;
    private UUID id;
    private String userName;
    private String email;
    private List<String> role;
    private String type = "Bearer";
    private String refreshToken;
    
    public JwtResponse(String token, UUID id, String userName, String email, List<String> role, String refreshToken) {
        this.token = token;
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.role = role;
        this.refreshToken = refreshToken;
    }
}
