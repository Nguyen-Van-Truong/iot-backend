package com.example.iotbackend.service.impl;

import com.example.iotbackend.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * EmailServiceImpl provides the implementation of EmailService,
 * specifically handling the sending of OTP (One-Time Password) emails to users.
 */
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    /**
     * Sends an OTP email to the user asynchronously.
     *
     * @param toEmail The recipient's email address.
     * @param otp The OTP (One-Time Password) to be sent to the user.
     */
    @Override
    @Async("taskExecutor")
    public void sendOtpEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        // Set the sender's email address
        message.setFrom(fromEmail);
        // Set the recipient's email address
        message.setTo(toEmail);
        message.setSubject("Your Password Reset OTP");
        // Set the content of the email (includes OTP and validity time)
        message.setText("Your OTP for password reset is: " + otp + "\nThis OTP is valid for 5 minutes.");

        mailSender.send(message);
    }
}
