package com.itpkg.core.tasks;

import com.itpkg.core.services.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by flamen on 15-7-23.
 */
@Component("core.job.session")
public class SessionJob {
    private final static Logger logger = LoggerFactory.getLogger(SessionJob.class);

    public void run() {
        logger.info("JOB BEGIN");
        logger.info("JOB END");
    }

    @Autowired
    SessionService sessionService;
}
