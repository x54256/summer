package cn.x5456.summer.env;

import cn.hutool.core.util.StrUtil;
import cn.x5456.summer.util.ReflectUtils;

import java.util.List;
import java.util.Properties;

/**
 * @author yujx
 * @date 2020/04/20 13:24
 */
public class PropertySourcesPropertyResolver implements PropertyResolver {

    // 在 Spring 中采用的是这个对象： private final PropertySources propertySources;
    private List<Properties> propertySources;

    public PropertySourcesPropertyResolver(List<Properties> propertySources) {
        this.propertySources = propertySources;
    }

    @Override
    public String getProperty(String key) {
        for (Properties propertySource : propertySources) {
            String value = (String) propertySource.get(key);
            if (StrUtil.isNotBlank(value)) {
                return value;
            }
        }
        return null;
    }

    @Override
    public <T> T getProperty(String key, Class<T> targetType) {
        return ReflectUtils.string2BasicType(this.getProperty(key), targetType);
    }
}
