package com.itpkg.core.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by flamen on 15-7-28.
 */
@Component("core.rssJob")
public class RssJob {
    private final static Logger logger = LoggerFactory.getLogger(RssJob.class);

    @Scheduled(cron = "0 0 */6 * * *")
    public void run() {
        if(enable) {
            logger.info("begin rss job");
            logger.info("end rss job");
        }
    }

    @Value("${job.dispatcher}")
    boolean enable;
}
