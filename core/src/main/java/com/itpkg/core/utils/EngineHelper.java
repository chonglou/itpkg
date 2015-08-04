package com.itpkg.core.utils;

import com.itpkg.core.services.SettingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by flamen on 15-7-21.
 */
@Component("core.utils.engine")
@Slf4j
public class EngineHelper {


    public boolean isEnable(String id) {
        return settingService.get("site.engine." + id + ".enable", Boolean.class) == Boolean.TRUE;
    }

    public void setEnable(String id, boolean enable) {
        settingService.set("site.engine." + id + ".enable", enable);
    }

    public void register(String id) {
        log.info("Load engine " + id);
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
