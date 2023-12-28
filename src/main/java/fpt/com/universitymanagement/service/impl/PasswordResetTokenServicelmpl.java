package fpt.com.universitymanagement.service.impl;

import fpt.com.universitymanagement.entity.PasswordResetToken;
import fpt.com.universitymanagement.entity.account.Account;
import fpt.com.universitymanagement.repository.AccountRepository;
import fpt.com.universitymanagement.repository.PasswordResetTokenRepository;
import fpt.com.universitymanagement.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.Random;

@Service
public class PasswordResetTokenServicelmpl implements PasswordResetService {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;
    @Autowired
    JavaMailSender mailSender;

    @Override
    public String forgotPassword(String email) {
        Account account = accountRepository.findByEmail(email).orElse(null);
        if (account == null) {
            return "Email is not accurate";
        }
        String token = generateResetToken();
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(token);
        passwordResetToken.setAccount(account);
        passwordResetToken.setExpiryDate(Instant.now());
        passwordResetTokenRepository.save(passwordResetToken);
        sendResetEmail(email, token);
        return "The email has been sent to the address " + email + ". Please check your email to reset your password.";
    }

    @Override
    public String resetPassword(String resetToken, String newPassword) {
        Optional<PasswordResetToken> getToken = passwordResetTokenRepository.findByToken(resetToken);
        if (getToken.isEmpty()) {
            return "Token is not valid";
        }
        if (getToken.get().getExpiryDate().isBefore(Instant.now().minus(Duration.ofDays(1)))) {
            return "Token is expired 1 day";
        }
        Account account = getToken.get().getAccount();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        account.setPassword(encodedPassword);
        passwordResetTokenRepository.delete(getToken.get());
        accountRepository.save(account);
        return "Password has been successfully reset.";
    }

    @Override
    public String generateResetToken() {
        // Generate a random 6-digit numeric OTP
        Random random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    @Override
    public void sendResetEmail(String email, String token) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("huuvule255@gmail.com");
        message.setTo(email);
        message.setText("OTP:" + token);
        message.setSubject("Reset Password");
        mailSender.send(message);

    }
}
