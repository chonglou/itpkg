package com.itpkg.tools.tasks;

import com.itpkg.tools.config.Constants;
import com.itpkg.tools.utils.CommandHelper;
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
public class Docker implements CommandMarker {
    private static final Logger logger = LoggerFactory.getLogger(Docker.class);
    private final String NAME_HELP = "Service name, like mail/vpn...";

    @CliCommand(value = "service setup", help = "Setup server")
    public String setup(
            @CliOption(key = {"name"}, mandatory = true, help = NAME_HELP)
            String name
    ) {
        switch (name) {
            case "mail":
                break;
            default:
                return "Not supported!";
        }
        return Constants.SUCCESS;
    }

    @CliCommand(value = "start", help = "Start server")
    public void start(
            @CliOption(key = {"service name"}, mandatory = true, help = NAME_HELP)
            String name
    ) {
        sshHelper.run("docker start name");
    }

    @CliCommand(value = "service stop", help = "Stop server")
    public void stop(@CliOption(key = {"name"}, mandatory = true, help = NAME_HELP)
                     String name) {
        sshHelper.run("docker stop " + name);
    }

    @CliCommand(value = "service list", help = "List docker services")
    public void services() {
        sshHelper.run("docker ps -a");
    }


    @Autowired
    CommandHelper sshHelper;

}
