package com.jianhongl.fresh.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * @author lijianhong Date: 2023/5/26 Time: 4:58 PM
 * @version $
 */
public class IgnorableObjectMapper extends ObjectMapper {

    public IgnorableObjectMapper() {
        super();
        setConfig(getDeserializationConfig().without(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES));
        configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        this.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }
}
