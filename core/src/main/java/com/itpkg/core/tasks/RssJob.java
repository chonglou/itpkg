package com.itpkg.core.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by flamen on 15-7-23.
 */
@Component("core.job.rss")
public class RssJob {
    private final static Logger logger = LoggerFactory.getLogger(RssJob.class);

    public void run() {
        logger.info("JOB BEGIN");
        logger.info("JOB END");
    }
}
