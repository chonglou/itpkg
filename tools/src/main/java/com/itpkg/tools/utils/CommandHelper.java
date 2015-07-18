package com.itpkg.tools.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by flamen on 15-7-17.
 */
@Component
public class CommandHelper {
    private static final Logger logger = LoggerFactory.getLogger(CommandHelper.class);

    public void ssh(String... commands) {
        //fixme
        for (String cmd : commands) {
            logger.debug("remote - "+cmd);
        }

    }

    public void run(String... commands) {
        //fixme
        for (String cmd : commands) {
            logger.debug("local - "+cmd);
        }

    }
}
