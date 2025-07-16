package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class OTPService {

    @Autowired
    private JavaMailSender mailSender;

    public String generateOtp() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    public void sendOtpEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your OTP Code");
        message.setText("Use this OTP to verify your email: " + otp);
        mailSender.send(message);
    }
}
