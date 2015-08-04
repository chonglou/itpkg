package com.itpkg.core.environments.development;

import com.itpkg.core.utils.EmailHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Created by flamen on 15-8-3.
 */
@Component
@Profile("!production")
public class EmailHelperImpl implements EmailHelper {
    private final static Logger logger = LoggerFactory.getLogger(EmailHelperImpl.class);

    @Override
    public void sendHtml(String to, String subject, String body) {
        logger.debug("send mail: {}\n{}", to, body);
    }
}
