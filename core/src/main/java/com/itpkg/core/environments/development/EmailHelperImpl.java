package com.itpkg.core.environments.development;

import com.itpkg.core.utils.EmailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Created by flamen on 15-8-3.
 */
@Component
@Profile("production")
@Slf4j
public class EmailHelperImpl implements EmailHelper {
    @Override
    public void sendHtml(String to, String subject, String body) {
        log.debug("send mail: {}\n{}", to, body);
    }
}
