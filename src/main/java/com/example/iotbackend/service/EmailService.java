package com.example.iotbackend.service;

/**
 * EmailService defines operations related to sending emails.
 */
public interface EmailService {

    /**
     * Sends an OTP email to the specified recipient.
     *
     * @param toEmail The recipient's email address.
     * @param otp The OTP to be sent.
     */
    void sendOtpEmail(String toEmail, String otp);
}
