package fpt.com.universitymanagement.service;

public interface PasswordResetService {
    String forgotPassword(String email);
    String resetPassword(String resetToken, String newPassword);
     String generateResetToken();

    void sendResetEmail(String email, String token);

}
