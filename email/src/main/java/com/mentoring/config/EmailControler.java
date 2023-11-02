package com.mentoring.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/apiEmail")
@RestController
public class EmailControler {
    private static final Logger logger = LoggerFactory.getLogger(EmailControler.class);
    private final JavaMailSender javaMailSender;
    private final SimpleMailMessage simpleMailMessage;
    private final EmailService emailService;

    public EmailControler(JavaMailSender javaMailSender, SimpleMailMessage simpleMailMessage, EmailService emailService) {
        this.javaMailSender = javaMailSender;
        this.simpleMailMessage = simpleMailMessage;
        this.emailService = emailService;
    }

    @GetMapping("/sendMail")
    public String sendConfirmationEmail() {
        simpleMailMessage.setTo("recipient@gmail.com");
        simpleMailMessage.setSubject("Testing from Spring Boot Travel Insurance");
        simpleMailMessage.setText("Travel Insurance from Spring Boot Email");
        javaMailSender.send(simpleMailMessage);
        logger.info("Email sent successfully!" + simpleMailMessage);
        return "Email sent successfully!";
    }

    @GetMapping("/sendAtt")
    public void sendAttachmentEmail() throws Exception {
        try {
        simpleMailMessage.setTo("recipient@gmail.com");
        simpleMailMessage.setSubject("Testing from Spring Boot Travel Insurance");
        simpleMailMessage.setText("Travel Insurance from Spring Boot Email");
        emailService.send();
        } catch (MailException ex) {
            ex.printStackTrace();
            System.err.println(ex.getMessage());
        }
    }
}
