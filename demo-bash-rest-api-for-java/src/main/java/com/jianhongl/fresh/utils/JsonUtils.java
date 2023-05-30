package com.jianhongl.fresh.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lijianhong Date: 2023/5/14 Time: 9:14 PM
 * @version $
 */
public class JsonUtils {

    private static final Logger LOG = LoggerFactory.getLogger(JsonUtils.class);

    public static IgnorableObjectMapper objectMapper = new IgnorableObjectMapper();

    static {

    }

    public static String writeValue(Object o) {
        try {
            return writeValueThrowException(o);
        } catch (Exception e) {
            return "";
        }
    }

    public static String writeValueThrowException(Object o) throws JsonProcessingException {

        try {
            return objectMapper.writeValueAsString(o);
        } catch (Exception e) {
            LOG.error("exception occur when Json serialize", e);
            throw e;
        }
    }



    public static <T> T readValue(String json, Class<T> clazz) {
        try {
            return readValueThrowException(json, clazz);
        } catch (IOException e) {
            return null;
        }
    }

    public static <T> T readValueThrowException(String json, Class<T> clazz) throws IOException {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            LOG.warn("Failed to deserialize from JSON string", e);
            throw e;
        }
    }

    public static <T> T readValue(String json, TypeReference<T> typeReference) {
        try {
            return readValueThrowException(json, typeReference);
        } catch (IOException e) {
            return null;
        }
    }

    public static <T> T readValueThrowException(String json, TypeReference<T> typeReference) throws IOException {
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (IOException e) {
            LOG.warn("Failed to deserialize from JSON string", e);
            throw e;
        }
    }

    public static Map<String, String> jsonToMap(String json) {
        try {
            return objectMapper.readValue(json, TYPE_MAP);
        } catch (IOException e) {
            LOG.warn("Failed to deserialize from JSON string", e);
            return null;
        }
    }

    public static <K, V> Map<K, V> jsonToMap(String json, TypeReference<Map<K, V>> typeReference) {
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (IOException e) {
            throw new RuntimeException("Failed to deserialize from JSON string", e);
        }
    }

    public static <T> List<T> jsonToList(String json, TypeReference<List<T>> typeReference) {
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final TypeReference<HashMap<String, String>> TYPE_MAP =
        new TypeReference<HashMap<String, String>>() {

        };
}
