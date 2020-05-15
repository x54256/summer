package cn.x5456.summer.core.env;

import cn.hutool.core.util.StrUtil;
import cn.x5456.summer.core.util.ReflectUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        // TODO: 2020/4/20 只对基本类型进行了支持，数组、List、Map等还不支持
        return ReflectUtils.string2BasicType(this.getProperty(key), targetType);
    }

    @Override
    public Map<String, String> getPropertyByPrefix(String prefix) {

        Map<String, String> map = new HashMap<>();
        for (Properties propertySource : propertySources) {
            propertySource.forEach((k, v) -> {
                String str = String.valueOf(k);
                if (str.startsWith(prefix + ".")) {
                    map.put(str.substring((prefix + ".").length()), String.valueOf(v));
                }
            });
        }
        return map;
    }
}
