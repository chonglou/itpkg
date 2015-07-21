package com.itpkg.core.utils;

import com.itpkg.core.services.SettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by flamen on 15-7-21.
 */
@Component("core.utils.engine")
public class EngineHelper {
    private static final Logger logger = LoggerFactory.getLogger(EngineHelper.class);

    public boolean isEnable(String id) {
        return settingService.get("site.engine." + id + ".enable", Boolean.class) == Boolean.TRUE;
    }

    public void setEnable(String id, boolean enable) {
        settingService.set("site.engine." + id + ".enable", enable);
    }

    public void register(String id) {
        logger.info("Load engine " + id);
        engines.add(id);
    }


    @PostConstruct
    void init() {
        engines = new ArrayList<>();
    }

    @Autowired
    SettingService settingService;

    private List<String> engines;


    public List<String> getEngines() {
        return engines;
    }
}
