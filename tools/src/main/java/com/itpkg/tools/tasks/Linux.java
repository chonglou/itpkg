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
public class Linux implements CommandMarker {
    private static final Logger logger = LoggerFactory.getLogger(Linux.class);

    @CliCommand(value = "os deploy", help = "Deploy it-package")
    public String deploy(
            @CliOption(key = {"os"}, mandatory = true, help = "Linux distribution name, like ubuntu/centos...")
            String os) {
        switch (os) {
            case "ubuntu":
                commandHelper.run(
                        "sudo apt-get -y install docker-io"
                );
                break;
            case "centos":
                commandHelper.run(
                        "sudo yum -y install docker",
                        "sudo service docker start"
                );
                break;
            default:
                return "Unknown linux distribution " + os;

        }
        commandHelper.run(
                "sudo useradd -s /bin/sh -m ops",
                "sudo passwd -l ops",
                "sudo gpasswd -a ops docker"
        );

        return Constants.SUCCESS;
    }

    @CliCommand(value = "os upgrade", help = "Upgrade system")
    public String upgrade() {
        commandHelper.run("docker pull " + Constants.BASE_IMAGE);
        commandHelper.ssh("pacman-key --populate archlinux",
                "pacman-key --refresh-keys",
                "pacman -Syu --noconfirm",
                "pacman-db-upgrade",
                "pacman -Scc --noconfirm"
        );
        return Constants.SUCCESS;
    }


    @CliCommand(value = "os info", help = "System info")
    public String show() {
        commandHelper.run("uname -a", "docker version");
        return Constants.SUCCESS;
    }

    @Autowired
    CommandHelper commandHelper;
}
