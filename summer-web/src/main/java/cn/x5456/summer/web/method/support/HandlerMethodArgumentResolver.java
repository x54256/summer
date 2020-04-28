package cn.x5456.summer.web.method.support;

import cn.x5456.summer.core.MethodParameter;
import cn.x5456.summer.web.bind.WebDataBinder;
import cn.x5456.summer.web.request.ServletWebRequest;

/**
 * 参数解析器
 */
public interface HandlerMethodArgumentResolver {

    /**
     * 此解析器是否支持给定的{@linkplain MethodParameter 方法参数}。
     */
    boolean supportsParameter(MethodParameter parameter);

    /**
     * 将方法参数解析为给定请求的参数值。
     * <p>
     * {@link ModelAndViewContainer}提供对请求模型的访问。
     * <p>
     * {@link WebDataBinderFactory}提供了一种在需要进行数据绑定和类型转换时创建{@link WebDataBinder}实例的方法。
     */
    Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                           ServletWebRequest webRequest, WebDataBinderFactory binderFactory);

}