package cn.x5456.summer.core.env;

/**
 * @author yujx
 * @date 2020/04/20 09:33
 */
public interface PropertyResolver {

    String getProperty(String key);

    <T> T getProperty(String key, Class<T> targetType);
}
