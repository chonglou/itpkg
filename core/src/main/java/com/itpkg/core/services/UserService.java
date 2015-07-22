package com.itpkg.core.services;

import com.itpkg.core.dao.LogDao;
import com.itpkg.core.dao.UserDao;
import com.itpkg.core.models.Log;
import com.itpkg.core.models.User;
import com.itpkg.core.utils.EncryptHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.UserProfile;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * Created by flamen on 15-7-14.
 */
@Service("core.service.user")
public class UserService {
    private final static Logger logger = LoggerFactory.getLogger(UserService.class);

    public User auth(String email, String password) {
        User u = userDao.findByEmail(email);
        if (u != null) {
            if (encryptHelper.check(password, u.getPassword())) {
                log(u, i18n.T("logs.user.sign_in.success"), Log.Type.INFO);
                return u;
            } else {
                log(u, i18n.T("form.user.sign_in.failed"), Log.Type.ERROR);
            }
        }
        return null;

    }

    public User findById(long id) {
        return userDao.findOne(id);
    }

    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }


    public void setAccessToken(long id, String accessToken) {
        User u = userDao.findOne(id);
        u.setAccessToken(accessToken);
        u.setUpdated(new Date());
        userDao.save(u);
    }


    public User create(String username, String email, String password) {
        User u = userDao.findByEmail(email);
        if (u == null) {
            u = new User();
            u.setUsername(username);
            u.setEmail(email);
            u.setPassword(encryptHelper.password(password));
            u.setProviderId("email");
            u.setProviderUserId(UUID.randomUUID().toString());


            Date now = new Date();
            u.setUpdated(now);
            u.setCreated(now);
            if (root.equals(email)) {
                u.setConfirmed(new Date());
            }

            userDao.save(u);
            log(u, i18n.T("logs.user.sign_up.success"), Log.Type.INFO);
            if (root.equals(email)) {
                roleService.set(u.getId(), "root");
                roleService.set(u.getId(), "admin");
            }

            return u;
        }
        return null;
    }

    public User create(UserProfile profile, ConnectionKey key, ConnectionData data) {
        User u = new User();
        u.setUsername(profile.getName());
        u.setEmail(profile.getEmail());
        u.setProviderId(key.getProviderId());
        u.setProviderUserId(key.getProviderUserId());
        u.setAccessToken(data.getAccessToken());

        Date now = new Date();
        u.setConfirmed(now);
        u.setUpdated(now);
        u.setCreated(now);

        userDao.save(u);
        return u;
    }


    public void log(User user, String message, Log.Type type) {
        Log l = new Log();
        l.setUser(user);
        l.setMessage(message);
        l.setType(type);
        l.setCreated(new Date());
        logDao.save(l);
    }


    @Autowired
    LogDao logDao;
    @Autowired
    RoleService roleService;
    @Autowired
    UserDao userDao;
    @Value("${root.email}")
    String root;
    @Autowired
    EncryptHelper encryptHelper;
    @Autowired
    I18nService i18n;

}
