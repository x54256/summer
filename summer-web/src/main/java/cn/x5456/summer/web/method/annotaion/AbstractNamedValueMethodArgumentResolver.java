package cn.x5456.summer.web.method.annotaion;

import cn.hutool.core.util.ObjectUtil;
import cn.x5456.summer.core.MethodParameter;
import cn.x5456.summer.web.bind.WebDataBinder;
import cn.x5456.summer.web.method.support.HandlerMethodArgumentResolver;
import cn.x5456.summer.web.method.support.ModelAndViewContainer;
import cn.x5456.summer.web.method.support.WebDataBinderFactory;
import cn.x5456.summer.web.request.ServletWebRequest;

/**
 * 参数解析的抽象父类，模版方法
 *
 * @author yujx
 * @date 2020/04/28 09:19
 */
public abstract class AbstractNamedValueMethodArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     * 将方法参数解析为给定请求的参数值。
     * <p>
     * {@link ModelAndViewContainer}提供对请求模型的访问。
     * <p>
     * {@link WebDataBinderFactory}提供了一种在需要进行数据绑定和类型转换时创建{@link WebDataBinder}实例的方法。
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, ServletWebRequest webRequest, WebDataBinderFactory binderFactory) {

        // 获取参数的信息
        NamedValueInfo namedValueInfo = this.createNamedValueInfo(parameter);

        // 解析参数的值
        Object arg = this.resolveName(namedValueInfo.name, parameter, webRequest);

        // 如果值为空，看是否必须和是否有默认值
        if (ObjectUtil.isEmpty(arg)) {
            if (namedValueInfo.defaultValue != null) {
                arg = namedValueInfo.defaultValue;
            } else {
                if (namedValueInfo.required) {
                    throw new RuntimeException("缺少请求的参数！");
                } else {
                    arg = null;
                }
            }
        }

        // 使用 binderFactory 将参数修改称为合适的类型
        WebDataBinder binder = binderFactory.createBinder(webRequest, null, namedValueInfo.name);
        arg = binder.convertIfNecessary(arg, parameter.getParameterType(), parameter);

        return arg;
    }

    protected abstract NamedValueInfo createNamedValueInfo(MethodParameter parameter);

    protected abstract Object resolveName(String name, MethodParameter parameter, ServletWebRequest webRequest);

    protected static class NamedValueInfo {

        private final String name;

        private final boolean required;

        private final String defaultValue;

        public NamedValueInfo(String name, boolean required, String defaultValue) {
            this.name = name;
            this.required = required;
            this.defaultValue = defaultValue;
        }
    }
}
