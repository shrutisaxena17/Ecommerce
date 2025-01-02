package com.example.Ecommerce.service;
import com.example.Ecommerce.entity.User;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class OtpService {

    private static final long OTP_VALIDITY_DURATION = 2 * 60;
    private Map<String, String> otpStore = new HashMap<>();
    private Map<String, Instant> otpTimestampStore = new HashMap<>();
    private Map<String, User> userStore = new HashMap<>();

    @Autowired
    private JavaMailSender mailSender;

    public String generateOtp(String registrationId) {
        String otp = String.valueOf((int) ((Math.random() * 900000) + 100000));
        otpStore.put(registrationId,otp);
        otpTimestampStore.put(registrationId, Instant.now());
        return otp;
    }

    public void sendOtpByEmail(String email, String otp) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(email);
            helper.setSubject("Your OTP Code");
            helper.setText("Dear Member,\n\nYour OTP code is: " + otp + "\n\nBest regards,\nYour Ecommerce Application");

            mailSender.send(mimeMessage);
            System.out.println("OTP sent to email: " + email);
        } catch (jakarta.mail.MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void storeUserData(String registrationId, User user) {
        userStore.put(registrationId, user);
    }

    public boolean isOtpValid(String registrationId, String otp) {
        Instant timestamp = otpTimestampStore.get(registrationId);
        long elapsedTime = Instant.now().getEpochSecond() - timestamp.getEpochSecond();
        if (timestamp == null || !otpStore.containsKey(registrationId)) {
            return false;
        }
        if (elapsedTime > OTP_VALIDITY_DURATION) {
            otpStore.remove(registrationId);
            otpTimestampStore.remove(registrationId);
            userStore.remove(registrationId);
            return false;
        }
        return otp.equals(otpStore.get(registrationId));
    }

    public User getStoredUserData(String registrationId) {
        return userStore.get(registrationId);
    }
}

