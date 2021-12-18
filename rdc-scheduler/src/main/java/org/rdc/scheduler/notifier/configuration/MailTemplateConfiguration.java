package org.rdc.scheduler.notifier.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailTemplateConfiguration {
    @Value("${template.entry}")
    public String templateEntry;

    @Value("${template.corruption}")
    public String templateCorruption;

    @Value("${mail.username}")
    public String mailUsername;

    @Value("${mail.password}")
    public String mailPassword;

    @Value("${mail.host}")
    public String mailHost;

    @Value("${mail.port}")
    public Integer mailPort;

    @Bean
    public SimpleMailMessage templateEntryMessage() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setText(templateEntry);
        return message;
    }

    @Bean
    public SimpleMailMessage templateCorruptionMessage() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setText(templateCorruption);
        return message;
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailHost);
        mailSender.setPort(mailPort);

        mailSender.setUsername(mailUsername);
        mailSender.setPassword(mailPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return mailSender;
    }
}
