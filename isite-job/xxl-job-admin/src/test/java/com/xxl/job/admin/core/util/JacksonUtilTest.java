package com.xxl.job.admin.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class JacksonUtilTest {

    @Test
    public void shouldWriteValueAsString() {
        //given
        Map<String, String> map = new HashMap<>();
        map.put("aaa", "111");
        map.put("bbb", "222");

        //when
        String json = JacksonUtil.writeValueAsString(map);

        //then
        Assertions.assertEquals(json, "{\"aaa\":\"111\",\"bbb\":\"222\"}");
    }

    @Test
    public void shouldReadValueAsObject() {
        //given
        String jsonString = "{\"aaa\":\"111\",\"bbb\":\"222\"}";

        //when
        Map result = JacksonUtil.readValue(jsonString, Map.class);

        //then
        Assertions.assertEquals(result.get("aaa"), "111");
        Assertions.assertEquals(result.get("bbb"),"222");

    }
}
