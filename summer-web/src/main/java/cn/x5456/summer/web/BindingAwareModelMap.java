package cn.x5456.summer.web;

import java.util.Map;

/**
 * @author yujx
 * @date 2020/04/27 16:49
 */
public class BindingAwareModelMap extends ModelMap implements Model {
    @Override
    public Model addAttribute(String attributeName, Object attributeValue) {
        super.put(attributeName, attributeValue);
        return this;
    }

    @Override
    public boolean containsAttribute(String attributeName) {
        return super.containsKey(attributeName);
    }

    @Override
    public Map<String, Object> asMap() {
        return this;
    }
}
