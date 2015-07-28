package com.itpkg.core.tasks;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.DatabaseSequenceFilter;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.filter.ITableFilter;
import org.dbunit.dataset.xml.FlatDtdDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.dbunit.ext.postgresql.PostgresqlDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by flamen on 15-7-27.
 */
@Component("core.backupJob")
public class BackupJob {
    private final static Logger logger = LoggerFactory.getLogger(BackupJob.class);

    @Scheduled(cron = "0 0 3 * * *")
    public void run() throws SQLException, DatabaseUnitException, IOException {
        if (enable) {
            logger.info("begin backup database");

            String bak = root + "/" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

            ITableFilter filter = new DatabaseSequenceFilter(conn);
            IDataSet dataSet = new FilteredDataSet(filter, conn.createDataSet());

            //IDataSet dataSet = conn.createDataSet();
            FlatXmlDataSet.write(dataSet, new FileOutputStream(bak + ".xml"));
            FlatDtdDataSet.write(dataSet, new FileOutputStream(bak + ".dtd"));
            logger.info("end backup database");
        }


    }

    public void load(File file) throws IOException, SQLException, DatabaseUnitException {
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(file);
        DatabaseOperation.CLEAN_INSERT.execute(conn, dataSet);
    }


    @PostConstruct
    void init() throws SQLException, DatabaseUnitException {
        new File(root).mkdirs();
        conn = new DatabaseConnection(dataSource.getConnection());
        DatabaseConfig config = conn.getConfig();
        switch (driver) {
            case "com.mysql.jdbc.Driver":
                config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new MySqlDataTypeFactory());
                break;
            case "org.postgresql.Driver":
                config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new PostgresqlDataTypeFactory());
                break;
            default:
                logger.info("Not support database: " + driver);
        }
    }

    private final String root = "tmp/backups";
    private IDatabaseConnection conn;

    @Value("${job.dispatcher}")
    boolean enable;
    @Value("${database.driver}")
    String driver;
    @Autowired
    DataSource dataSource;

}
