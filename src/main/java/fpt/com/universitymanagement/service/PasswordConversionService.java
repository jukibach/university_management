package fpt.com.universitymanagement.service;

public interface PasswordConversionService {
    void forgotPassword(String email);

    void resetPassword(String otp, String newPassword, String confirmPassword);

    String generateResetToken();

    void sendResetEmail(String email, String token);

}
