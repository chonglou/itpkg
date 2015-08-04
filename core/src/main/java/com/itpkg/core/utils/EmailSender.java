package com.itpkg.core.utils;

import com.itpkg.core.models.Mail;
import com.itpkg.core.models.Setting;
import com.itpkg.core.models.Smtp;
import com.itpkg.core.services.SettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.mail.internet.MimeMessage;

/**
 * Created by flamen on 15-8-3.
 */
@Component
@DependsOn("core.siteHelper")
public class EmailSender {
    private final static Logger logger = LoggerFactory.getLogger(EmailSender.class);


    @PostConstruct
    public void init() {
        smtp = settingService.get("site.smtp", Smtp.class);
        sender = new JavaMailSenderImpl();
    }

    @Autowired
    SettingService settingService;
    private JavaMailSenderImpl sender;
    private Smtp smtp;

    public JavaMailSenderImpl get() {
        return sender;
    }

    public void setSender(JavaMailSenderImpl sender) {
        this.sender = sender;
    }

    public Smtp getSmtp() {
        return smtp;
    }
}
