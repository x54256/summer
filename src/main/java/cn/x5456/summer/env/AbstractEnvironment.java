package cn.x5456.summer.env;

import cn.hutool.core.util.ArrayUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author yujx
 * @date 2020/04/20 13:29
 */
public abstract class AbstractEnvironment implements Environment {

    // 在 Spring 中采用的是这个对象： private final PropertySources propertySources;
    private final List<Properties> propertySources = new ArrayList<>();

    private final PropertyResolver propertyResolver = new PropertySourcesPropertyResolver(this.propertySources);

    public AbstractEnvironment() {
        this.customizePropertySources(this.propertySources);
    }

    // 子类实现，添加一些 properties
    protected void customizePropertySources(List<Properties> propertySources) {

    }

    @Override
    public String[] getActiveProfiles() {
        String[] activeProfiles = this.getProperty("activeProfiles", String[].class);
        if (ArrayUtil.isNotEmpty(activeProfiles)) {
            return activeProfiles;
        }

        return this.getDefaultProfiles();
    }

    @Override
    public String[] getDefaultProfiles() {
        return new String[]{"default"};
    }

    @Override
    public String getProperty(String key) {
        return propertyResolver.getProperty(key);
    }

    @Override
    public <T> T getProperty(String key, Class<T> targetType) {
        return propertyResolver.getProperty(key, targetType);
    }

    public void addProperties(Properties properties) {
        propertySources.add(properties);
    }
}
