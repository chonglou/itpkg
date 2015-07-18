package com.itpkg.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by flamen on 15-7-17.
 */
@Component
public class EmailHelper {
    private final Logger logger = LoggerFactory.getLogger(EmailHelper.class);
    public void send(String to, String subject, String body){
        //todo
        logger.debug("MAIL-TO: "+to+"\n"+subject+"\n"+body);
    }
}
