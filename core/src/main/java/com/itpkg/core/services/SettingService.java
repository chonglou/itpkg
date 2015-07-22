package com.itpkg.core.services;

import com.itpkg.core.dao.SettingDao;
import com.itpkg.core.models.Setting;
import com.itpkg.core.utils.EncryptHelper;
import com.itpkg.core.utils.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by flamen on 15-7-14.
 */
@Service
public class SettingService {
    private final static Logger logger = LoggerFactory.getLogger(SettingService.class);

    public void set(String key, Object val) {
        set(key, val, false);
    }

    public void set(String key, Object val, boolean encode) {

        String json = jsonHelper.object2json(val);
        Setting s = settingDao.findByKey(key);
        Date now = new Date();
        if (s == null) {
            s = new Setting();
            s.setKey(key);
        }
        s.setEncode(encode);
        s.setVal(encode ? encryptHelper.encrypt(json) : json);
        s.setUpdated(now);
        settingDao.save(s);
    }

    public <T> T get(String key, Class<T> clazz) {
        Setting s = settingDao.findByKey(key);
        if (s != null) {
            String json = s.isEncode() ? encryptHelper.decrypt(s.getVal()) : s.getVal();
            return jsonHelper.json2object(json, clazz);
        }

        return null;
    }


    @Autowired
    SettingDao settingDao;
    @Autowired
    EncryptHelper encryptHelper;
    @Autowired
    JsonHelper jsonHelper;

}
