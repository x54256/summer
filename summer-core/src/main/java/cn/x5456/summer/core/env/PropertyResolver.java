package cn.x5456.summer.core.env;

import java.util.Map;

/**
 * @author yujx
 * @date 2020/04/20 09:33
 */
public interface PropertyResolver {

    String getProperty(String key);

    <T> T getProperty(String key, Class<T> targetType);

    /**
     * 根据前缀获取配置文件中的属性
     *
     * @return map 的 key 为属性名（不包含prefix），value 为属性值，只支持基本类型
     */
    Map<String, String> getPropertyByPrefix(String prefix);
}
