package com.itpkg.core.tasks;

import com.itpkg.core.services.SettingService;
import com.itpkg.core.utils.EmailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created by flamen on 15-7-28.
 */
@Component("core.emailJob")
public class EmailJob {
    private final static Logger logger = LoggerFactory.getLogger(EmailJob.class);

    @RabbitListener(queues = "email")
    public void onMessage(Message<byte[]> message) throws MessagingException, IOException {

        try (ByteArrayInputStream bais = new ByteArrayInputStream(message.getPayload())) {
            MimeMessage mm = new MimeMessage(null, bais);
            logger.debug("send mail: {} ", mm.getMessageID());
            sender.get().send(mm);
        }

    }


    @Autowired
    SettingService settingService;
    @Autowired
    EmailSender sender;
}
