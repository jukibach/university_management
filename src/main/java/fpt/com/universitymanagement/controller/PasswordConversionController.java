package fpt.com.universitymanagement.controller;

import fpt.com.universitymanagement.service.PasswordConversionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static fpt.com.universitymanagement.common.Constant.AUTH_CONTROLLER;

@RestController
@RequestMapping(AUTH_CONTROLLER)
public class PasswordConversionController {

    @Autowired
    private PasswordConversionService passwordConversionService;

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        passwordConversionService.forgotPassword(email);
        return ResponseEntity.ok("The OTP code has been sent to your email");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String otp, @RequestParam String newPassword, @RequestParam String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            return ResponseEntity.badRequest().body("incorrect password");
        }
        try {
            passwordConversionService.resetPassword(otp, newPassword, confirmPassword);
            return ResponseEntity.ok("Password reset successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error resetting password: " + e.getMessage());
        }
    }
}
