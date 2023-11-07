package com.kakashi.codeboxbank.service.impl;



import com.kakashi.codeboxbank.dto.EmailDetails;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements  EmailService {

    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private  String senderEmail;
    @Override
    public void sendEmailAlert(EmailDetails emailDetails) {

    try {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(senderEmail);
        msg.setTo(emailDetails.getRecipient());
        msg.setSubject(emailDetails.getSubject());
        msg.setText(emailDetails.getMessageBody());
        javaMailSender.send(msg);
    }catch (MailException e){

        throw new RuntimeException("Error while sending email" + e.getMessage());

    }
    }
}
