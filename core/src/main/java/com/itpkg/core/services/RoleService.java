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
@Service("core.service.role")
public class RoleService {

    public boolean check(long user, String name) {
        return check(user, name, null, null);
    }

    public boolean check(long user, String name, String rType) {
        return check(user, name, rType, null);
    }

    public boolean check(long user, String name, String rType, Long rId) {
        Date now = new Date();
        Role r = roleDao.find(user, name, rType, rId);
        return r != null && now.after(r.getStartUp()) && now.before(r.getShutDown());
    }


    public void delete(long user) {
        roleDao.delete(user);
    }

    public void delete(long user, String name) {
        roleDao.delete(user, name);
    }

    public void delete(long user, String name, String rType) {
        roleDao.delete(user, name, rType);
    }

    public void delete(long user, String name, String rType, Long rId) {
        roleDao.delete(user, name, rType, rId);
    }

    public void set(long user, String name) {
        set(user, name, null, null, min(), max());
    }

    public void set(long user, String name, String rType) {
        set(user, name, rType, null, min(), max());
    }

    public void set(long user, String name, String rType, long rId) {
        set(user, name, rType, rId, min(), max());
    }

    public void set(long user, String name, String rType, Long rId, Date startUp, Date shutDown) {
        Role r = roleDao.find(user, name, rType, rId);
        Date now = new Date();
        if (r == null) {
            r = new Role();
            r.setUser(userDao.findOne(user));
            r.setName(name);
            r.setCreated(now);
        }
        r.setStartUp(startUp);
        r.setShutDown(shutDown);
        r.setUpdated(now);
        roleDao.save(r);
    }


    private Date max() {
        //return Date.from(LocalDateTime.MAX.atZone(ZoneId.systemDefault()).toInstant());
        return Date.from(LocalDateTime.of(9999, 12, 31, 23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());
    }

    private Date min() {
        //return Date.from(LocalDateTime.MIN.atZone(ZoneId.systemDefault()).toInstant());
        return Date.from(LocalDateTime.of(2015, 8, 4, 0, 0, 0).atZone(ZoneId.systemDefault()).toInstant());
    }

    @Autowired
    RoleDao roleDao;
    @Autowired
    UserDao userDao;
}
