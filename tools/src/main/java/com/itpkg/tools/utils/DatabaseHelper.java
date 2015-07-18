package com.itpkg.tools.utils;

import com.itpkg.tools.models.Database;
import com.jolbox.bonecp.BoneCPDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by flamen on 15-7-17.
 */
public class DatabaseHelper {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseHelper.class);

    void init(Database db) throws ClassNotFoundException {

        Class.forName(db.getDriver());
        BoneCPDataSource ds = new BoneCPDataSource();
        ds.setJdbcUrl(db.getUrl());
        ds.setUsername(db.getUser());
        ds.setPassword(db.getPassword());
        jdbcTemplate.setDataSource(ds);

        String now = jdbcTemplate.queryForObject("SELECT CURRENT_TIMESTAMP ", String.class);
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS `virtual_domains` (" +
                "  `id` int(11) NOT NULL auto_increment," +
                "  `name` varchar(50) NOT NULL," +
                "  PRIMARY KEY (`id`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS `virtual_users` (" +
                "  `id` int(11) NOT NULL auto_increment," +
                "  `domain_id` int(11) NOT NULL," +
                "  `password` varchar(106) NOT NULL," +
                "  `email` varchar(100) NOT NULL," +
                "  PRIMARY KEY (`id`)," +
                "  UNIQUE KEY `email` (`email`)," +
                "  FOREIGN KEY (domain_id) REFERENCES virtual_domains(id) ON DELETE CASCADE" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS `virtual_aliases` (" +
                "  `id` int(11) NOT NULL auto_increment," +
                "  `domain_id` int(11) NOT NULL," +
                "  `source` varchar(100) NOT NULL," +
                "  `destination` varchar(100) NOT NULL," +
                "  PRIMARY KEY (`id`)," +
                "  FOREIGN KEY (domain_id) REFERENCES virtual_domains(id) ON DELETE CASCADE" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8");

        logger.info("Database time:" + now);

    }

    JdbcTemplate jdbcTemplate;
}
