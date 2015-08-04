package com.itpkg.core.tasks;

import com.itpkg.core.models.Mail;
import com.itpkg.core.services.SettingService;
import com.itpkg.core.utils.EmailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * Created by flamen on 15-7-28.
 */
@Component("core.emailJob")
public class EmailJob {
    private final static Logger logger = LoggerFactory.getLogger(EmailJob.class);

    @RabbitListener(queues = "email")
    public void onMessage(Message<byte[]> message)  throws MessagingException, IOException{
        MimeMessage mm = new MimeMessage(sender.get(),  new ByteArrayInputStream(message.getPayload()));

        logger.debug("send mail: {} ", mm.getMessageID());

        sender.get().send(mm);

        sender.get().send(mm);
//            SimpleMailMessage smm = new SimpleMailMessage();
//            smm.setFrom(m.getFrom());
//            smm.setText(m.getBody());
//            smm.setSubject(m.getSubject());
//            smm.setBcc(m.getBcc().toArray(new String[1]));
//            smm.setCc(m.getCc().toArray(new String[1]));
//            sender.get().send(smm);

    }
//    public void onMessage(Message<Map<String, Object>> message)  {
//        logger.debug("### "+message.getPayload().get("body"));
//    }


    @Autowired
    SettingService settingService;
    @Autowired
    EmailSender sender;
}
