package com.itpkg.email.services;

import com.itpkg.core.web.widgets.SelectField;
import com.itpkg.email.dao.EmailAliasDao;
import com.itpkg.email.dao.EmailDomainDao;
import com.itpkg.email.dao.EmailUserDao;
import com.itpkg.email.models.Alias;
import com.itpkg.email.models.Domain;
import com.itpkg.email.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by flamen on 15-7-18.
 */
@Service("email.service")
public class EmailService {
    private final Logger logger = LoggerFactory.getLogger(EmailService.class);

    public Domain getDomain(long id) {
        return domainDao.findOne(id);
    }

    public SelectField<Long> getDomainMap() {
        SelectField<Long> s = new SelectField<>(null);
        domainDao.findAll().forEach((d) -> s.addOption(d.getId(), d.getName()));
        return s;
    }

    public SelectField<Long> getUserMap() {
        SelectField<Long> s = new SelectField<>(null);
        userDao.findAll().forEach((u) -> s.addOption(u.getId(), u.getEmail()));
        return s;
    }

    public List<Domain> listAllDomain() {
        return domainDao.findAll();
    }

    public Domain createDomain(String name) {
        return domainDao.findByName(name).map((d) -> (Domain) null).orElseGet(() -> {
            Domain d = new Domain();
            d.setName(name);
            Date now = new Date();
            d.setCreated(now);
            d.setUpdated(now);
            domainDao.save(d);
            logger.info("add email domain " + name);
            return d;
        });

    }

    public void removeDomain(long domain) {
        Domain d = domainDao.findOne(domain);
        if (d != null) {
            aliasDao.deleteByDomainId(domain);
            userDao.deleteByDomainId(domain);
            domainDao.delete(domain);
            logger.info("del email domain " + d.getName());
        }
    }


    public Alias createAlias(long domain, String source, String destination) {
        Domain d = domainDao.findOne(domain);
        if (d == null) {
            return null;
        }
        String src = source + "@" + d.getName();
        String dst = destination + "@" + d.getName();


        return userDao.findByEmail(dst).map((ud) ->
                        userDao.findByEmail(src).map((us) -> (Alias) null).orElseGet(() -> {
                            Alias a = new Alias();
                            a.setSource(src);
                            a.setDestination(dst);
                            Date now = new Date();
                            a.setCreated(now);
                            a.setUpdated(now);
                            logger.info("add email alias " + a.getSource() + " => " + a.getDestination());
                            return a;

                        })
        ).orElse(null);
    }

    public void removeAlias(long alias) {
        Alias a = aliasDao.findOne(alias);
        if (a != null) {
            aliasDao.delete(alias);
            logger.info("del email alias " + a.getSource());

        }
    }


    public User createUser(long domain, String name, String password) {
        Domain d = domainDao.findOne(domain);
        if (d == null) {
            return null;
        }
        String email = name + "@" + d.getName();
        return userDao.findByEmail(email).map((u) -> (User) null).orElseGet(() -> {
            User u = new User();
            u.setEmail(email);
            u.setDomain(d);

            u.setPassword(password); //fixme

            Date now = new Date();
            u.setUpdated(now);
            u.setCreated(now);
            userDao.save(u);
            logger.info("add mail user " + email);
            return u;
        });
    }

    public void removeUser(long user) {
        User u = userDao.findOne(user);
        if (u != null) {
            aliasDao.deleteByDestination(u.getEmail());
            userDao.delete(user);
            logger.info("del email user " + u.getEmail());
        }
    }

    @Autowired
    EmailUserDao userDao;
    @Autowired
    EmailDomainDao domainDao;
    @Autowired
    EmailAliasDao aliasDao;

}
