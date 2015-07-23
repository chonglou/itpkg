package com.itpkg.core.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by flamen on 15-7-15.
 */
@Component("core.utils.json")
public class JsonHelper {
    private static final Logger logger = LoggerFactory.getLogger(JsonHelper.class);

    public <K, V> Map<K, V> json2map(String json) {
        try {
            TypeReference<HashMap<K, V>> type = new TypeReference<HashMap<K, V>>() {
            };
            return mapper.readValue(json, type);
        } catch (IOException e) {
            logger.error("parse json to map error", e);
        }
        return null;
    }

    public String object2json(Object obj) {
        if (obj != null) {
            try {
                return mapper.writeValueAsString(obj);
            } catch (IOException e) {
                logger.error("generate json error", e);
            }
        }
        return null;
    }

    public <T> T json2object(String json, Class<T> clazz) {
        if (json != null) {
            try {
                return mapper.readValue(json, clazz);
            } catch (IOException e) {
                logger.error("parse json error", e);
            }
        }
        return null;
    }


    @PostConstruct
    void init() {
        mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    private ObjectMapper mapper;
}
