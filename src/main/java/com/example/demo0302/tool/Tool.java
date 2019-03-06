package com.example.demo0302.tool;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

/**
 * @author: yiqq
 * @date: 2019/2/28
 * @description: 工具类
 */
public class Tool {
    /**
     * json转化成对象
     * @param json
     * @param clazz
     * @param <T>
     * @return
     * @throws IOException
     */
    public synchronized static <T> T json2Object(String json,Class<T> clazz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.readValue(json, clazz);
    }

    /**
     * map转换为json
     * @param map
     * @return
     * @throws IOException
     */
    public synchronized static String map2Json(Map map) throws IOException {
        if (isEmpty(map)) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(map);
    }

    public static boolean isEmpty(Map map) {
        return (null != map && !map.isEmpty()) ? false : true;
    }
}
