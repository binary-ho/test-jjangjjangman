package com.example.demo.user.service;

import com.example.demo.user.service.port.MailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CertificationService {

    private static final String MAIL_TITLE = "Please certify your email address";
    private static final String MAIL_CONTENT = "Please click the following link to certify your email address: %s";

    private final MailSender mailSender;

    public void send(String email, Long userId, String certificationCode) {
        String certificationUrl = generateCertificationUrl(userId, certificationCode);
        mailSender.send(email, MAIL_TITLE, String.format(MAIL_CONTENT, certificationUrl));
    }

    private String generateCertificationUrl(Long userId, String certificationCode) {
        return "http://localhost:8080/api/users/" + userId + "/verify?certificationCode=" + certificationCode;
    }
}
