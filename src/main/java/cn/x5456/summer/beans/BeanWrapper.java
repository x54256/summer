package cn.x5456.summer.beans;

import cn.hutool.core.util.ReflectUtil;

import java.util.List;

/**
 * 对 bean 进行包装，封装一些注入属性的方法
 *
 * @author yujx
 * @date 2020/04/16 17:29
 */
public class BeanWrapper {

    private Object bean;

    public BeanWrapper(Object bean) {
        this.bean = bean;
    }

    public void setPropertyValue(PropertyValue pv) {

        // 字段名
        String name = pv.getName();

        // 值
        Object value = pv.getValue();

        // 设置值
        ReflectUtil.setFieldValue(bean, name, value);
    }

    public void setPropertyValues(List<PropertyValue> pvs) {
        for (PropertyValue pv : pvs) {
            this.setPropertyValue(pv);
        }
    }

    public Object getWrappedInstance() {
        return bean;
    }
}
