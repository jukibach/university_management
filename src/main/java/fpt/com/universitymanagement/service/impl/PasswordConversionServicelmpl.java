package fpt.com.universitymanagement.service.impl;

import fpt.com.universitymanagement.entity.PasswordConversion;
import fpt.com.universitymanagement.entity.account.Account;
import fpt.com.universitymanagement.repository.AccountRepository;
import fpt.com.universitymanagement.repository.PasswordConversionRepository;
import fpt.com.universitymanagement.service.PasswordConversionService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.Random;

@Service
public class PasswordConversionServicelmpl implements PasswordConversionService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordConversionRepository passwordConversionRepository;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private TemplateEngine templateEngine;
    private static final Logger log = LoggerFactory.getLogger(PasswordConversionServicelmpl.class);

    @Override
    public void forgotPassword(String email) {
        Account account = accountRepository.findByEmail(email).orElse(null);
        if (account == null) {
            return;
        }
        String token = generateResetToken();
        PasswordConversion passwordConversion = new PasswordConversion();
        passwordConversion.setToken(token);
        passwordConversion.setAccount(account);
        passwordConversion.setExpiryDate(Instant.now());
        passwordConversionRepository.save(passwordConversion);
        sendResetEmail(email, token);
    }

    @Override
    public void resetPassword(String token, String newPassword, String confirmPassword) {
        Optional<PasswordConversion> getToken = passwordConversionRepository.findByToken(token);
        if (getToken.isEmpty()) {
            return;
        }
        if (getToken.get().getExpiryDate().isBefore(Instant.now().minus(Duration.ofDays(1)))) {
            return;
        }
        Account account = getToken.get().getAccount();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        account.setPassword(encodedPassword);
        passwordConversionRepository.delete(getToken.get());
        accountRepository.save(account);
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
        Context context = new Context();
        String resetLink = "http://huuvu/reset-password?OTP=" + token;
        context.setVariable("resetLink", resetLink);
        String htmlContent = templateEngine.process("reset_password.html", context);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom("huuvule255@gmail.com");
            helper.setTo(email);
            helper.setSubject("Reset Password");
            helper.setText(htmlContent, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("Unable to send email:", e);
        }
    }
}
