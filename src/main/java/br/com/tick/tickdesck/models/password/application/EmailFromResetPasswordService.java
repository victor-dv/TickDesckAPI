package br.com.tick.tickdesck.models.password.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailFromResetPasswordService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendResetPasswordEmail(String to, String token) {
        String resetUrl = "URL_PAGE_AQUI" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Redefinição de senha - TickDesck");
        message.setText("Clique aqui para redefinir sua senha:\n" + resetUrl);
        mailSender.send(message);
    }

}
