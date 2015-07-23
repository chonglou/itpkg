package com.itpkg.core.services;

import com.itpkg.core.dao.SessionDao;
import com.itpkg.core.models.Session;
import com.itpkg.core.utils.EncryptHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by flamen on 15-7-23.
 */
@Service("core.service.session")
public class SessionService {

    public void saveToken(String token) {
        Session ss = new Session();
        ss.setPayload(getPayload(token));
        ss.setCreated(new Date());
        sessionDao.save(ss);
    }

    public Session getByToken(String token) {
        return sessionDao.findByPayload(getPayload(token));
    }

    public void delByToken(String token) {
        sessionDao.deleteByPayload(getPayload(token));
    }

    private String getPayload(String token) {
        String[] ss = token.split("\\.");
        if (ss.length != 3) {
            throw new IllegalArgumentException("bad token");
        }
        return encryptHelper.fromBase64(ss[1]);
    }

    @Autowired
    SessionDao sessionDao;
    @Autowired
    EncryptHelper encryptHelper;
}
