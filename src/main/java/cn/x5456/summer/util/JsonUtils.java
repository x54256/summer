package cn.x5456.summer.util;

import cn.hutool.core.io.FileUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * json解析工具类
 *
 * @author yujx
 */
public final class JsonUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static String toString(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj.getClass() == String.class) {
            return (String) obj;
        }
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public static <T> T toBean(String json, Class<T> tClass) {
        try {
            return mapper.readValue(json, tClass);
        } catch (IOException e) {
            return null;
        }
    }

    public static <E> List<E> toList(String json, Class<E> eClass) {
        try {
            return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, eClass));
        } catch (IOException e) {
            return null;
        }
    }

    public static <K, V> Map<K, V> toMap(String json, Class<K> kClass, Class<V> vClass) {
        try {
            return mapper.readValue(json, mapper.getTypeFactory().constructMapType(HashMap.class, kClass, vClass));
        } catch (IOException e) {
            return null;
        }
    }

    public static <K, V> Map<K, V> toLinkedMap(String json, Class<K> kClass, Class<V> vClass) {
        try {
            return mapper.readValue(json, mapper.getTypeFactory().constructMapType(LinkedHashMap.class, kClass, vClass));
        } catch (IOException e) {
            return null;
        }
    }

    public static <T> T nativeRead(String json, TypeReference<T> type) {
        try {
            return mapper.readValue(json, type);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 导出文件
     */
    public static File dump(Object o, String filePath) {
        String json = JsonUtils.toString(o);
        return FileUtil.writeUtf8String(json, filePath);
    }

    /**
     * 解析json文件转成java对象
     */
    public static <E> List<E> load2List(String filePath, Class<E> eClass) {
        String json = FileUtil.readUtf8String(filePath);
        return JsonUtils.toList(json, eClass);
    }

    private JsonUtils() {
    }
}
