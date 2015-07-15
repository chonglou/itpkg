package com.itpkg;

import com.itpkg.core.services.SettingService;
import com.itpkg.core.services.UserService;
import com.jolbox.bonecp.BoneCPDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Arrays;

/**
 * Created by flamen on 15-7-14.
 */

@Configuration
@ComponentScan
@PropertySource("classpath:config.properties")
@EnableAutoConfiguration
public class Application {
    private final Logger logger = LoggerFactory.getLogger(Application.class);
    @Bean
    CommandLineRunner init(UserService userService,
                           SettingService settingService) {
        return (evt) -> Arrays.asList(
                "root,admin".split(","))
                .forEach(
                        a -> {
                            logger.info("Start application");
                        });
    }

//    @Bean(destroyMethod = "close")
//    BoneCPDataSource dataSource(){
//        String driver = env.getProperty("database.driver");
//        BoneCPDataSource ds = new BoneCPDataSource();
//        ds.setJdbcUrl(env.getProperty("database.url"));
//        ds.setDriverClass(driver);
//        ds.setMaxConnectionsPerPartition(30);
//        ds.setMinConnectionsPerPartition(10);
//        ds.setPartitionCount(3);
//        ds.setAcquireIncrement(5);
//        ds.setStatementsCacheSize(100);
//
//        switch (driver){
//            case "org.apache.derby.jdbc.EmbeddedDriver":
//                break;
//            default:
//                ds.setUsername(env.getProperty("database.username"));
//                ds.setPassword(env.getProperty("database.password"));
//                break;
//        }
//        return ds;
//    }
//    @Resource
//    private Environment env;
//
//    public void setEnv(Environment env) {
//        this.env = env;
//    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


}
