package com.itpkg.core.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itpkg.core.dao.SessionDao;
import com.itpkg.core.models.Session;
import com.itpkg.core.utils.EncryptHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by flamen on 15-7-23.
 */
@Service("core.sessionService")
public class SessionService {
    private final static Logger logger = LoggerFactory.getLogger(SessionService.class);

    public void delByExpire() {
        ObjectMapper mapper = new ObjectMapper();
        List<Long> ids = new ArrayList<>();

        sessionDao.findAll().forEach((s) -> {
            try {
                JsonNode node = mapper.readTree(s.getPayload());
                if (node.get("exp").asLong() < new Date().getTime()) {
                    ids.add(s.getId());
                }
            } catch (IOException e) {
                logger.error("parse json error", e);
            }
        });
        ids.forEach(sessionDao::delete);
    }

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
