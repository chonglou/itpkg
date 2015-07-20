package com.itpkg.core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * Created by flamen on 15-7-15.
 */
@Component("core.utils.json")
public class JsonHelper {

    public String object2json(Object obj) throws IOException {
        return objectMapper.writeValueAsString(obj);
    }

    public <T> T json2object(String json, Class<T> clazz) throws IOException {
        return objectMapper.readValue(json, clazz);
    }


    @PostConstruct
    void init() {
        objectMapper = new ObjectMapper();
        //objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
    }

    private ObjectMapper objectMapper;
}
