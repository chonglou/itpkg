package com.itpkg.tools.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;

/**
 * Created by flamen on 15-7-17.
 */
@Component
public class ConfigHelper {
    private static final Logger logger = LoggerFactory.getLogger(ConfigHelper.class);

    public boolean ok() {
        return new File(filename).exists();
    }

    public void write(Object obj) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(obj);
        } catch (IOException e) {
            logger.error("Fail on write config file", e);
        }
    }

    public <T> T read(Class<T> clazz) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            T t = (T) ois.readObject();
            return t;
        } catch (IOException | ClassNotFoundException e) {
            logger.error("Fail on read config file", e);
        }
        return null;
    }

    @PostConstruct
    void init() {
        filename = "config.bin";
    }

    private String filename;
}
