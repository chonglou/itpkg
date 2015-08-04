package com.itpkg.core.environments.production;

import com.itpkg.core.models.Mail;
import com.itpkg.core.models.Smtp;
import com.itpkg.core.services.SettingService;
import com.itpkg.core.utils.EmailHelper;
import com.itpkg.core.utils.EmailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;


/**
 * Created by flamen on 15-8-3.
 */
@Component
@Profile("!production")
public class EmailHelperImpl implements EmailHelper {
    private final static Logger logger = LoggerFactory.getLogger(EmailHelperImpl.class);

    @Override
    public void sendHtml(String to, String subject, String body)  {

       send(true, null, new String[]{to}, null, subject, body, null);
    }
    private void send(boolean html, String from, String[] to, String bcc, String subject, String body, Map<String, String> files)  {

        Smtp smtp = sender.getSmtp();

        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            MimeMessage mm = sender.get().createMimeMessage();
            MimeMessageHelper mmh;
            if (html) {
                mmh = new MimeMessageHelper(mm, true);
            } else {
                mmh = new MimeMessageHelper(mm);
                for (Map.Entry<String, String> en : files) {
                    mmh.addAttachment(en.getKey(), new File(en.getValue()));

                }
            }


            mmh.setFrom(from);
            mmh.setText(body);
            mmh.setSubject(subject);
            mmh.setBcc(m.getBcc().toArray(new String[1]));
            mmh.setCc(m.getCc().toArray(new String[1]));
            mm.writeTo(baos);

            amqpTemplate.convertAndSend("email", baos.toByteArray());

        }
        catch (MessagingException|IOException e){
            logger.error("generate eml error", e);
        }

//        Mail m = new Mail();
//        m.setHtml(html);
//        m.setSubject(subject);
//        m.setBody(body);
//        m.setFiles(files);
//        m.setFrom(from == null ? smtp.getFrom() : from);
//        m.getTo().addAll(Arrays.asList(to));
//        if(bcc == null){
//            m.getBcc().addAll(smtp.getBcc());
//        }else {
//            m.getBcc().add(bcc);
//        }

    }

    @Autowired
    AmqpTemplate amqpTemplate;
    @Autowired
    SettingService settingService;
    @Autowired
    EmailSender sender;


}
