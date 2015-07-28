package com.itpkg.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by flamen on 15-7-17.
 */
@Component("core.utils.email")
public class EmailHelper {
    private final static Logger logger = LoggerFactory.getLogger(EmailHelper.class);

    public void send(String to, String subject, String body) {
        Message msg = MessageBuilder
                .withBody(body.getBytes())
                .setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN)
                .setHeader("to", to)
                .setHeader("subject", subject)
                .setTimestamp(new Date())
                .build();
        amqpTemplate.send("email", msg);
    }

    @Autowired
    AmqpTemplate amqpTemplate;
}
