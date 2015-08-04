package com.itpkg.core.tasks;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by flamen on 15-7-28.
 */
@Component("core.rssJob")
@Slf4j
public class RssJob {


    @Scheduled(cron = "0 0 */6 * * *")
    public void run() {
        if (enable) {
            log.info("begin rss job");
            log.info("end rss job");
        }
    }

    @Value("${job.dispatcher}")
    boolean enable;
}
