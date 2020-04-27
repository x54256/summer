package cn.x5456.summer.web.method.support;

import cn.x5456.summer.core.MethodParameter;
import cn.x5456.summer.web.bind.WebDataBinder;
import cn.x5456.summer.web.request.ServletWebRequest;

import java.util.LinkedList;
import java.util.List;

public class HandlerMethodArgumentResolverComposite implements HandlerMethodArgumentResolver {

    private final List<HandlerMethodArgumentResolver> argumentResolvers = new LinkedList<>();

    /**
     * 此解析器是否支持给定的{@linkplain MethodParameter 方法参数}。
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return this.getArgumentResolver(parameter) != null;
    }

    private HandlerMethodArgumentResolver getArgumentResolver(MethodParameter parameter) {
        for (HandlerMethodArgumentResolver argumentResolver : argumentResolvers) {
            if (argumentResolver.supportsParameter(parameter)) {
                return argumentResolver;
            }
        }
        return null;
    }

    /**
     * 将方法参数解析为给定请求的参数值。
     * <p>
     * {@link ModelAndViewContainer}提供对请求模型的访问。
     * <p>
     * {@link WebDataBinderFactory}提供了一种在需要进行数据绑定和类型转换时创建{@linkplain WebDataBinder}实例的方法。
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, ServletWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HandlerMethodArgumentResolver resolver = this.getArgumentResolver(parameter);
        if (resolver == null) {
            throw new IllegalArgumentException("Unknown parameter type [" + parameter.getParameterType().getName() + "]");
        }

        // 使用解析器进行解析
        return resolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
    }

    public HandlerMethodArgumentResolverComposite addResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        argumentResolvers.addAll(resolvers);
        return this;
    }
}