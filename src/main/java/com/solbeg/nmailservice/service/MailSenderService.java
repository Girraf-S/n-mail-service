package com.solbeg.nmailservice.service;

import com.solbeg.nmailservice.model.UserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailSenderService {
    private final JavaMailSender mailSender;
    @Value("${service.user-domain}")
    private String userDomain;
    @Value("${spring.mail.username}")
    private String username;
    @Value("${spring.mail.admin-mail}")
    private String adminMail;

    public void verifyEmail(String code, String email) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        String subject = "Activation code";
        String message = "Hello! To verify your email visit link '"
                + userDomain + "account/verify-mail/" + code +
                "'";
        mailMessage.setFrom(username);
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }

    public void sendUserInfoToAdmin(UserRequest user) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        String subject = "Activate user with id";

        String message = "User: " +
                user.toString() +
                "\n" +
                "Activate user: link '" +
                userDomain +
                "admin/activate/" +
                user.getId() +
                "'";

        mailMessage.setFrom(user.getEmail());
        mailMessage.setTo(adminMail);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }
}
