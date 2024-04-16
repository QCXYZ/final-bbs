package com.bbs.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class EmailService {
    @Resource
    private JavaMailSender mailSender;

    /**
     * Send a simple email
     *
     * @param to      Email address of the recipient
     * @param subject Subject of the email
     * @param text    Body of the email
     */
    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("2339679990@qq.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    public void sendResetPasswordEmail(String email, String token) {
        // Logic to send email
        String resetLink = "http://localhost:8181/api/auth/reset-password?token=" + token;
        // Example: Send email using JavaMailSender or another email library
    }

    public void sendResetTokenEmail(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("2339679990@qq.com");
        message.setTo(to);
        message.setSubject("密码重置");
        // 应该使用换行符
        message.setText("您的密码重置验证码为: " + token
                + "\n验证码有效时间为5分钟"
                + "\n请点击以下链接重置密码: http://localhost:8181/api/auth/reset-password");
//        message.setText("验证码有效时间为5分钟");
//        message.setText("请点击以下链接重置密码: http://localhost:8181/api/auth/reset-password");
        mailSender.send(message);
    }
}
