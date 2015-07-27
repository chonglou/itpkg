package com.itpkg.core.services;

import com.itpkg.core.dao.ContactDao;
import com.itpkg.core.dao.LogDao;
import com.itpkg.core.dao.UserDao;
import com.itpkg.core.models.Contact;
import com.itpkg.core.models.Log;
import com.itpkg.core.models.User;
import com.itpkg.core.utils.EncryptHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by flamen on 15-7-14.
 */
@Service("core.service.user")
public class UserService {
    private final static Logger logger = LoggerFactory.getLogger(UserService.class);

    public void setPassword(long user, String password) {
        User u = userDao.findOne(user);
        if (u != null) {
            u.setPassword(encryptHelper.password(password));
            u.setUpdated(new Date());
            userDao.save(u);
            log(u, i18n.T("logs.user.change_password"), Log.Type.INFO);
        }
    }

    public void setLocked(long user, Date date) {
        User u = userDao.findOne(user);
        if (u != null) {
            u.setLocked(date);
            u.setUpdated(new Date());
            userDao.save(u);
            if (date == null) {
                log(u, i18n.T("logs.user.unlock"), Log.Type.INFO);
            } else {
                log(u, i18n.T("logs.user.lock"), Log.Type.INFO);
            }

        }
    }

    public void setConfirmed(long user) {
        User u = userDao.findOne(user);
        if (u != null) {
            u.setConfirmed(new Date());
            u.setUpdated(new Date());
            userDao.save(u);
            log(u, i18n.T("logs.user.confirm"), Log.Type.INFO);
        }
    }

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
            u.setProviderUserId(email);


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

            Contact c = new Contact();
            c.setUpdated(now);
            c.setCreated(now);
            c.setUser(u);
            contactDao.save(c);

            return u;
        }
        return null;
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
    @Autowired
    ContactDao contactDao;
    @Value("${root.email}")
    String root;
    @Autowired
    EncryptHelper encryptHelper;
    @Autowired
    I18nService i18n;

}
