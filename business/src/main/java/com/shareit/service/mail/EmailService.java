package com.shareit.service.mail;

import com.shareit.utils.commons.email.EmailDataModel;
import com.shareit.utils.commons.email.EmailSender;
import com.shareit.utils.commons.email.MailDetail;
import com.shareit.utils.commons.exception.EmailSenderException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

@Service
public class EmailService implements EmailSender {
    private final JavaMailSender mailSender;
    private final Configuration configuration;

    public EmailService(JavaMailSender mailSender, Configuration configuration) {
        this.mailSender = mailSender;
        this.configuration = configuration;
    }

    @Override
    @Async
    public CompletableFuture<Void> send(MailDetail request, EmailDataModel model) {
        CompletableFuture completableFuture = new CompletableFuture();
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
            Template t = configuration.getTemplate("emailConfirmation.ftl");
            model.setSubject(request.getSubject());

            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

            helper.setTo(request.getTo());
            helper.setFrom(request.getFrom());
            helper.setSubject(request.getSubject());
            helper.setText(html, true);
            mailSender.send(message);
            completableFuture.complete("");
        } catch (MessagingException | IOException | TemplateException e) {
            completableFuture.completeExceptionally(new EmailSenderException("failed to send email"));
        }
        return completableFuture;
    }
}
