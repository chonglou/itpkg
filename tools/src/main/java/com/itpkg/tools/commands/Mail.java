package com.itpkg.tools.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

/**
 * Created by flamen on 15-7-17.
 */
@Component
public class Mail implements CommandMarker {
    private static final Logger logger = LoggerFactory.getLogger(Mail.class);

    @CliAvailabilityIndicator({"mail add", "mail del", "mail list"})
    public boolean isMailAvailable() {
        return true;
    }

    @CliCommand(value = "mail add", help = "Add email user")
    public String addUser(
            @CliOption(key = {"email"}, mandatory = true, help = "Email")
            String email,
            @CliOption(key = {"password"}, mandatory = true, help = "Password")
            String password
    ) {
        logger.info("Will add email user " + email);
        return "add user";
    }

    @CliCommand(value = "mail del", help = "Remove email user")
    public String delUser(
            @CliOption(key = {"email"}, mandatory = true, help = "Email")
            String email
    ) {
        return "del user";
    }

    @CliCommand(value = "mail list", help = "List all email user")
    public String listUser() {
        return "list user";
    }


}
