package com.itpkg.core.tasks;

import com.itpkg.core.services.SettingService;
import com.itpkg.core.utils.EmailSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created by flamen on 15-7-28.
 */
@Component("core.emailJob")
@Slf4j
public class EmailJob {

    public void onMessage(byte[] message) throws MessagingException, IOException {

        try (ByteArrayInputStream bais = new ByteArrayInputStream(message)) {
            MimeMessage mm = new MimeMessage(null, bais);
            log.debug("send mail: {} ", mm.getMessageID());
            sender.get().send(mm);
        }

    }


    @Autowired
    SettingService settingService;
    @Autowired
    EmailSender sender;
}
