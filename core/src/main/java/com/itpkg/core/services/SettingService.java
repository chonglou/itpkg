package com.itpkg.core.services;

import com.itpkg.core.dao.SettingDao;
import com.itpkg.core.models.Setting;
import com.itpkg.core.utils.EncryptHelper;
import com.itpkg.core.utils.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        String json = jsonHelper.object2json(val);

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

    public <T> T get(String key, Class<T> clazz) throws IOException {

        String json = settingDao.findByKey(key)
                .map((s) -> s.isEncode() ? encryptHelper.decrypt(s.getVal()) : s.getVal())
                .orElse(null);

        return jsonHelper.json2object(json, clazz);
    }


    @Autowired
    private SettingDao settingDao;
    @Autowired
    private EncryptHelper encryptHelper;
    @Autowired
    private JsonHelper jsonHelper;

    public void setJsonHelper(JsonHelper jsonHelper) {
        this.jsonHelper = jsonHelper;
    }

    public void setEncryptHelper(EncryptHelper encryptHelper) {
        this.encryptHelper = encryptHelper;
    }

    public void setSettingDao(SettingDao settingDao) {
        this.settingDao = settingDao;
    }
}
