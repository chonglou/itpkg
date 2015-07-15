package com.itpkg.core.services;

import com.itpkg.core.dao.RoleDao;
import com.itpkg.core.dao.UserDao;
import com.itpkg.core.models.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by flamen on 15-7-15.
 */
@Service
public class RoleService {

    public boolean check(String user, String name) {
        return check(user, name, null, null);
    }

    public boolean check(String user, String name, String rType) {
        return check(user, name, rType, null);
    }

    public boolean check(String user, String name, String rType, Integer rId) {
        return roleDao.find(user, name, rType, rId).map((r) -> {
            Date now = new Date();
            return now.after(r.getStartUp()) && now.before(r.getShutDown());
        }).orElse(false);
    }

    public void delete(String user) {
        roleDao.delete(user);
    }

    public void delete(String user, String name) {
        roleDao.delete(user, name);
    }

    public void delete(String user, String name, String rType) {
        roleDao.delete(user, name, rType);
    }

    public void delete(String user, String name, String rType, Integer rId) {
        roleDao.delete(user, name, rType, rId);
    }

    public void set(String user, String name) {
        set(user, name, null, null, min(), max());
    }

    public void set(String user, String name, String rType) {
        set(user, name, rType, null, min(), max());
    }

    public void set(String user, String name, String rType, int rId) {
        set(user, name, rType, rId, min(), max());
    }

    public void set(String user, String name, String rType, Integer rId, Date startUp, Date shutDown) {
        roleDao.find(user, name, rType, rId).map((r) -> {
            r.setStartUp(startUp);
            r.setShutDown(shutDown);
            r.setUpdated(new Date());
            roleDao.save(r);
            return r;
        }).orElseGet(() -> {
            Role r = new Role();
            r.setUser(userDao.findOne(user));
            r.setName(name);
            r.setStartUp(startUp);
            r.setShutDown(shutDown);
            r.setUpdated(new Date());
            roleDao.save(r);
            return r;
        });
    }


    private Date max() {
        return Date.from(LocalDateTime.MAX.atZone(ZoneId.systemDefault()).toInstant());
    }

    private Date min() {
        return Date.from(LocalDateTime.MIN.atZone(ZoneId.systemDefault()).toInstant());
    }

    @Autowired
    RoleDao roleDao;
    @Autowired
    UserDao userDao;
}
