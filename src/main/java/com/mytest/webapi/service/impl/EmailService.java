package com.mytest.webapi.service.impl;

import com.mytest.webapi.model.dto.MailDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@Service
@Log4j2
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String mailAddress;

    public void sendEmail(MailDto mailDto){
        try {
            MimeMessage msg = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg,MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED
            , StandardCharsets.UTF_8.name());
            Context context = new Context();
            String html = templateEngine.process("email/email-template", context);
            helper.setTo(mailDto.getTo());
            helper.setText(html, true);
            helper.setSubject(mailDto.getSubject());
            helper.setFrom(mailAddress);
            emailSender.send(msg);
        }catch (Exception e){
            log.error("Could not send a message to the mail address: {}", mailDto);
            throw new RuntimeException(e);
        }
    }
}
