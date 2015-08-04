package com.itpkg.email.services;

import com.itpkg.core.utils.EngineHelper;
import com.itpkg.core.web.widgets.SelectField;
import com.itpkg.email.dao.DomainDao;
import com.itpkg.email.dao.EmailAliasDao;
import com.itpkg.email.dao.UserDao;
import com.itpkg.email.models.Alias;
import com.itpkg.email.models.Domain;
import com.itpkg.email.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

/**
 * Created by flamen on 15-7-18.
 */
@Service("email.service")
@Slf4j
public class EmailService {


    public Domain getDomain(long id) {
        return domainDao.findOne(id);
    }

    public SelectField<Long> getDomainMap(String id, String name) {
        SelectField<Long> s = new SelectField<>(id, name);
        domainDao.findAll().forEach((d) -> s.addOption(d.getId(), d.getName()));
        return s;
    }

    public SelectField<Long> getUserMap(String id, String name) {
        SelectField<Long> s = new SelectField<>(id, name);
        userDao.findAll().forEach((u) -> s.addOption(u.getId(), u.getEmail()));
        return s;
    }


    public List<Domain> listAllDomain() {
        return domainDao.findAll();
    }

    public Domain createDomain(String name) {
        Domain d = domainDao.findByName(name);
        if (d == null) {
            d = new Domain();
            d.setName(name);
            Date now = new Date();
            d.setCreated(now);
            d.setUpdated(now);
            domainDao.save(d);
            log.info("add email domain " + name);
            return d;
        }
        return null;
    }

    public void removeDomain(long domain) {
        Domain d = domainDao.findOne(domain);
        if (d != null) {
            aliasDao.deleteByDomainId(domain);
            userDao.deleteByDomainId(domain);
            domainDao.delete(domain);
            log.info("del email domain " + d.getName());
        }
    }


    public Alias createAlias(long domain, String source, String destination) {
        Domain d = domainDao.findOne(domain);
        if (d == null) {
            return null;
        }
        String src = source + "@" + d.getName();
        String dst = destination + "@" + d.getName();

        User ud = userDao.findByEmail(dst);
        if (ud != null) {
            User us = userDao.findByEmail(src);
            if (us == null) {
                Alias a = new Alias();
                a.setSource(src);
                a.setDestination(dst);
                Date now = new Date();
                a.setCreated(now);
                a.setUpdated(now);
                log.info("add email alias " + a.getSource() + " => " + a.getDestination());
                return a;
            }
        }
        return null;
    }

    public void removeAlias(long alias) {
        Alias a = aliasDao.findOne(alias);
        if (a != null) {
            aliasDao.delete(alias);
            log.info("del email alias " + a.getSource());

        }
    }


    public User createUser(long domain, String name, String password) {
        Domain d = domainDao.findOne(domain);
        if (d == null) {
            return null;
        }
        String email = name + "@" + d.getName();
        User u = userDao.findByEmail(email);
        if (u == null) {
            u = new User();
            u.setEmail(email);
            u.setDomain(d);

            u.setPassword(password); //fixme

            Date now = new Date();
            u.setUpdated(now);
            u.setCreated(now);
            userDao.save(u);
            log.info("add mail user " + email);
            return u;
        }
        return null;
    }

    public void removeUser(long user) {
        User u = userDao.findOne(user);
        if (u != null) {
            aliasDao.deleteByDestination(u.getEmail());
            userDao.delete(user);
            log.info("del email user " + u.getEmail());
        }
    }

    @PostConstruct
    void init() {
        engineHelper.register("email");
    }

    @Autowired
    UserDao userDao;
    @Autowired
    DomainDao domainDao;
    @Autowired
    EmailAliasDao aliasDao;
    @Autowired
    EngineHelper engineHelper;

}
