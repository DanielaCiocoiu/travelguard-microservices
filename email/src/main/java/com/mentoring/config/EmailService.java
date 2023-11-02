package com.mentoring.config;

import com.mentoring.service.policy.PolicyServiceIml;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailControler.class);

    private final JavaMailSender javaMailSender;
    private final PolicyServiceIml policyServiceIml;

    public EmailService(JavaMailSender mailSender, PolicyServiceIml policyServiceIml) {
        this.javaMailSender = mailSender;

        this.policyServiceIml = policyServiceIml;
    }

    public void sendEmail(MessageInfoDto message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(Objects.requireNonNull(simpleMailMessage.getTo()));
        simpleMailMessage.setFrom(message.getEmail());
        simpleMailMessage.setText(message.getText());
        simpleMailMessage.setSubject("New Message");
        simpleMailMessage.setReplyTo(message.getEmail());
        javaMailSender.send(simpleMailMessage);
    }

/*    public void sendMailWithAttachment(String to, String subject, String body, String fileToAttach) {
        try {
        MimeMessagePreparator preparator = mimeMessage -> {
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            mimeMessage.setFrom(new InternetAddress("admin@gmail.com"));
            mimeMessage.setSubject(subject);
            mimeMessage.setText(body);
            InputStreamResource file = new InputStreamResource(policyServiceIml.exportExcel());
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.addAttachment("Policies.xlsx", file);
            logger.info("Email with att sent successfully from controller!" + mimeMessage);
        };
            javaMailSender.send(preparator);
        } catch (MailException  ex) {
            ex.printStackTrace();
            System.err.println(ex.getMessage());
        }
    }*/
    public void send() {
        try {
            final ByteArrayOutputStream stream = createInMemoryDocument(policyServiceIml.exportExcel().toString());
            final InputStreamSource attachment = new ByteArrayResource(stream.toByteArray());
            sendMimeMessageWithAttachments(
                    "Testing from Spring Boot Travel Insurance",
                    "admin@gmail.com",
                    "daniela-mihaela.ciocoiu@endava.com",
                    attachment);
        } catch (IOException | MailException | MessagingException e) {
            logger.warn(e.getMessage(), e);
        }
    }
    private void sendMimeMessageWithAttachments(String subject, String from, String to, InputStreamSource source) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setSubject(subject);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setReplyTo(from);
        helper.setText("stub", false);
        helper.addAttachment("Policies.xlsx", source);
        javaMailSender.send(message);
    }
    private ByteArrayOutputStream createInMemoryDocument(String documentBody) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(documentBody.getBytes());
        return outputStream;
    }
}
