package com.itpkg.core.services;

import com.itpkg.core.dao.ContactDao;
import com.itpkg.core.dao.LogDao;
import com.itpkg.core.dao.UserDao;
import com.itpkg.core.models.Contact;
import com.itpkg.core.models.Log;
import com.itpkg.core.models.User;
import com.itpkg.core.utils.EncryptHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * Created by flamen on 15-7-14.
 */
@Service("core.userService")
public class UserService {

    public long count() {
        return userDao.count();
    }

    public void setPassword(long user, String password) {
        User u = userDao.findOne(user);
        if (u != null) {
            u.setPassword(encryptHelper.password(password));
            u.setAccessToken(UUID.randomUUID().toString());
            u.setUpdated(new Date());
            userDao.save(u);
            addLog(u, i18n.T("logs.user.change_password"), Log.Type.INFO);
        }
    }

    public void setLocked(long user, Date date) {
        User u = userDao.findOne(user);
        if (u != null) {
            u.setLocked(date);
            u.setUpdated(new Date());
            userDao.save(u);
            if (date == null) {
                addLog(u, i18n.T("logs.user.unlock"), Log.Type.INFO);
            } else {
                addLog(u, i18n.T("logs.user.lock"), Log.Type.INFO);
            }

        }
    }

    public void setConfirmed(long user) {
        User u = userDao.findOne(user);
        if (u != null) {
            u.setConfirmed(new Date());
            u.setUpdated(new Date());
            userDao.save(u);
            addLog(u, i18n.T("logs.user.confirm"), Log.Type.INFO);
        }
    }

    public User auth(String email, String password) {
        User u = userDao.findByEmail(email);
        if (u != null) {
            if (encryptHelper.check(password, u.getPassword())) {
                addLog(u, i18n.T("logs.user.sign_in.success"), Log.Type.INFO);
                return u;
            } else {
                addLog(u, i18n.T("form.user.sign_in.failed"), Log.Type.ERROR);
            }
        }
        return null;

    }

    public User findById(long id) {
        return userDao.findOne(id);
    }

    public User findByToken(String provider, String token) {
        return userDao.findByProviderIdAndAccessToken(provider, token);
    }

    public User findByEmail(String email) {
        return userDao.findByEmail(email);
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
            u.setAccessToken(UUID.randomUUID().toString());


            Date now = new Date();
            u.setUpdated(now);
            u.setCreated(now);

            userDao.save(u);
            addLog(u, i18n.T("logs.user.sign_up.success"), Log.Type.INFO);


            Contact c = new Contact();
            c.setUpdated(now);
            c.setCreated(now);
            c.setUser(u);
            contactDao.save(c);

            return u;
        }
        return null;
    }


    public void addLog(User user, String message, Log.Type type) {
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
    @Autowired
    EncryptHelper encryptHelper;
    @Autowired
    I18nService i18n;

}
