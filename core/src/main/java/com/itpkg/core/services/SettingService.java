package com.itpkg.core.services;

import com.itpkg.core.dao.SettingDao;
import com.itpkg.core.models.Setting;
import com.itpkg.core.utils.EncryptHelper;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Date;

/**
 * Created by flamen on 15-7-14.
 */
@Service
public class SettingService {
    private final Logger logger = LoggerFactory.getLogger(SettingService.class);

    public void set(String key, Object val) throws IOException {
        set(key, val, false);
    }

    public void set(String key, Object val, boolean encode) throws IOException {
        //String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(val);
        String json = objectMapper.writeValueAsString(val);

        settingDao.findByKey(key).map((s) -> {
            if (encode) {
                s.setVal(encryptHelper.encrypt(json));
            } else {
                s.setVal(json);
            }
            s.setEncode(encode);
            s.setUpdated(new Date());
            settingDao.save(s);
            return s;
        }).orElseGet(() -> {
            Setting s = new Setting();
            s.setKey(key);
            if (encode) {
                s.setVal(encryptHelper.encrypt(json));
            } else {
                s.setVal(json);
            }
            s.setEncode(encode);
            s.setCreated(new Date());
            s.setUpdated(new Date());
            settingDao.save(s);
            return s;
        });


    }

    public <T> T get(String key, String val, final Class<T> clazz) throws IOException {

        String json = settingDao.findByKey(key)
                .map((s) -> s.isEncode() ? encryptHelper.decrypt(s.getVal()) : s.getVal())
                .orElse(null);

        return json == null ? null : objectMapper.readValue(json, clazz);
    }


    @PostConstruct
    void init() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Autowired
    private SettingDao settingDao;
    @Autowired
    private EncryptHelper encryptHelper;
    private ObjectMapper objectMapper;

    public void setEncryptHelper(EncryptHelper encryptHelper) {
        this.encryptHelper = encryptHelper;
    }

    public void setSettingDao(SettingDao settingDao) {
        this.settingDao = settingDao;
    }
}
