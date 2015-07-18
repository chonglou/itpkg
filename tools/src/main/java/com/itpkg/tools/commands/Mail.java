package com.itpkg.tools.commands;

import com.itpkg.tools.config.Constants;
import com.itpkg.tools.models.Configuration;
import com.itpkg.tools.models.Database;
import com.itpkg.tools.utils.ConfigHelper;
import com.itpkg.tools.utils.MailHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

/**
 * Created by flamen on 15-7-17.
 */
@Component
public class Mail implements CommandMarker {
    private static final Logger logger = LoggerFactory.getLogger(Mail.class);

//    @CliAvailabilityIndicator({"mail create", "mail remove", "mail passwd", "mail all"})
//    public boolean isMailAvailable() {
//        return true;
//    }

    @CliCommand(value = "mail setup", help = "Setup database")
    public String setup(
            @CliOption(key = {"type"}, mandatory = false, help = "Database type(default: mysql) can be mysql,postgresql")
            String type,
            @CliOption(key = {"host"}, mandatory = false, help = "Database host(default: localhost)")
            String host,
            @CliOption(key = {"name"}, mandatory = false, help = "Database name(default: mail)")
            String name,
            @CliOption(key = {"port"}, mandatory = false, help = "Database port(default:3306)")
            String port,
            @CliOption(key = {"user"}, mandatory = false, help = "Database user(default: root)")
            String user,
            @CliOption(key = {"password"}, mandatory = false, help = "Database password(default: )")
            String password
    ) {
        Configuration cfg;
        if (configHelper.ok()) {
            cfg = configHelper.read(Configuration.class);
            if (cfg == null) {
                return Constants.FAILED;
            }
        } else {
            cfg = new Configuration();
            cfg.setMail(Database.mysql());
        }
        if (type != null) {
            cfg.getMail().setType(type);
        }
        if (host != null) {
            cfg.getMail().setHost(host);
        }
        if (port != null) {
            cfg.getMail().setPort(Integer.parseInt(port));
        }
        if (user != null) {
            cfg.getMail().setHost(user);
        }
        if (password != null) {
            cfg.getMail().setPassword(password);
        }
        logger.info(String.format("Using %s %s@%s:%d",
                cfg.getMail().getType(),
                cfg.getMail().getUser(),
                cfg.getMail().getHost(),
                cfg.getMail().getPort()));
        configHelper.write(cfg);
        try {
            mailHelper.init();
        } catch (ClassNotFoundException | IllegalArgumentException e) {
            logger.error("Database setup error", e);
        }


        return Constants.SUCCESS;
    }

    @CliCommand(value = "mail create", help = "Create new email user")
    public String addUser(
            @CliOption(key = {"email"}, mandatory = true, help = "Email")
            String email,
            @CliOption(key = {"password"}, mandatory = true, help = "Password")
            String password
    ) {
        logger.info("Will add email user " + email);
        return "add user";
    }

    @CliCommand(value = "mail passwd", help = "Set email password")
    public String password(
            @CliOption(key = {"email"}, mandatory = true, help = "Email")
            String email,
            @CliOption(key = {"password"}, mandatory = true, help = "Password")
            String password
    ) {
        logger.info("Will add email user " + email);
        return "password user";
    }

    @CliCommand(value = "mail remove", help = "Remove email user")
    public String delUser(
            @CliOption(key = {"email"}, mandatory = true, help = "Email")
            String email
    ) {
        return "del user";
    }

    @CliCommand(value = "mail all", help = "List all email user")
    public String listUser() {
        return "list user";
    }

    @Autowired
    ConfigHelper configHelper;
    @Autowired
    MailHelper mailHelper;

}
