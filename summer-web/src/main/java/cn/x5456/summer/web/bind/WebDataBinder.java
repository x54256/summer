package cn.x5456.summer.web.bind;

import cn.hutool.core.convert.Convert;
import cn.x5456.summer.core.MethodParameter;

/**
 * 我也不知道这2个参数有啥用
 */
public class WebDataBinder {

    private Object target;

    private String objectName;

    public WebDataBinder(Object target, String objectName) {
        this.target = target;
        this.objectName = objectName;
    }

    public Object getTarget() {
        return target;
    }

    public String getObjectName() {
        return objectName;
    }

    /**
     * 这里 hutool 的 Convert 的实现和 Spring 中的 DataBinder 非常相似
     *
     * @param parameter 这个参数在 Spring 中有用，因为太深，所以我没看
     */
    public <T> T convertIfNecessary(Object arg, Class<T> parameterType, MethodParameter parameter) {
        return Convert.convert(parameterType, arg);
    }
}
