package com.itpkg.core.environments.production;

import com.itpkg.core.models.Smtp;
import com.itpkg.core.services.SettingService;
import com.itpkg.core.utils.EmailHelper;
import com.itpkg.core.utils.EmailSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;


/**
 * Created by flamen on 15-8-3.
 */
@Component
@Profile("production")
@Slf4j
public class EmailHelperImpl implements EmailHelper {

    @Override
    public void sendHtml(String to, String subject, String body) {
        send(true, null, new String[]{to}, null, null, subject, body, null);
    }

    private void send(boolean html, String from, String[] to, String[] cc, String[] bcc, String subject, String body, Map<String, String> files) {

        Smtp smtp = sender.getSmtp();

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            MimeMessage mm = sender.get().createMimeMessage();
            MimeMessageHelper mmh;
            if (html) {
                mmh = new MimeMessageHelper(mm, true);
                if (files != null) {
                    for (Map.Entry<String, String> en : files.entrySet()) {
                        mmh.addInline(en.getKey(), new File(en.getValue()));
                    }
                }
            } else {
                mmh = new MimeMessageHelper(mm);
                if (files != null) {
                    for (Map.Entry<String, String> en : files.entrySet()) {
                        mmh.addAttachment(en.getKey(), new File(en.getValue()));
                    }
                }
            }

            if (from == null) {
                mmh.setFrom(smtp.getFrom());
            } else {
                mmh.setFrom(from);
            }
            mmh.setText(body);
            mmh.setTo(to);
            mmh.setSubject(subject);
            if (bcc == null) {
                mmh.setBcc(smtp.getBccArray());
            } else {
                mmh.setBcc(bcc);
            }
            if (cc != null) {
                mmh.setCc(cc);
            }
            mm.writeTo(baos);
            template.convertAndSend("email", baos.toByteArray());


        } catch (MessagingException | IOException e) {
            log.error("generate eml error", e);
        }


    }

    @Autowired
    AmqpTemplate template;

    @Autowired
    SettingService settingService;
    @Autowired
    EmailSender sender;


}
