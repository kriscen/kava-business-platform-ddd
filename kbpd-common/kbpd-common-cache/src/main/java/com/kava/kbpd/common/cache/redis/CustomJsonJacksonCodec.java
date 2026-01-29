package com.kava.kbpd.common.cache.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.redisson.codec.JsonJacksonCodec;

/**
 * @author Kris
 * @date 2026/1/29
 * @description:
 */
public class CustomJsonJacksonCodec extends JsonJacksonCodec {

    public CustomJsonJacksonCodec(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    protected void init(ObjectMapper objectMapper) {
    }

    @Override
    protected void initTypeInclusion(ObjectMapper mapObjectMapper) {

    }
}