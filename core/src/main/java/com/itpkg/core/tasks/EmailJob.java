package com.itpkg.core.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by flamen on 15-7-28.
 */
@Component("core.emailJob")
public class EmailJob {
    private final static Logger logger = LoggerFactory.getLogger(EmailJob.class);

    @RabbitListener(queues = "email")
    public void onMessage(Message message) {
        Map<String, Object> headers = message.getMessageProperties().getHeaders();
        String to = (String) headers.get("to");
        String from = (String) headers.get("from");
        String body = new String(message.getBody());
        logger.debug("send mail: {}\n{}", to, body);
    }
}
