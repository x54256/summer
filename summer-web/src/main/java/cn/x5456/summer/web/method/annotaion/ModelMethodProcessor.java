package cn.x5456.summer.web.method.annotaion;

import cn.x5456.summer.core.MethodParameter;
import cn.x5456.summer.web.Model;
import cn.x5456.summer.web.bind.WebDataBinder;
import cn.x5456.summer.web.method.support.HandlerMethodArgumentResolver;
import cn.x5456.summer.web.method.support.ModelAndViewContainer;
import cn.x5456.summer.web.method.support.WebDataBinderFactory;
import cn.x5456.summer.web.request.ServletWebRequest;

/**
 * 可以解析 Model 参数
 *
 * @author yujx
 * @date 2020/04/28 16:07
 */
public class ModelMethodProcessor implements HandlerMethodArgumentResolver {

    /**
     * 此解析器是否支持给定的{@linkplain MethodParameter 方法参数}。
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return Model.class.isAssignableFrom(parameter.getParameterType());
    }

    /**
     * 将方法参数解析为给定请求的参数值。
     * <p>
     * {@link ModelAndViewContainer}提供对请求模型的访问。
     * <p>
     * {@link WebDataBinderFactory}提供了一种在需要进行数据绑定和类型转换时创建{@link WebDataBinder}实例的方法。
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, ServletWebRequest webRequest, WebDataBinderFactory binderFactory) {
        return mavContainer.getModel();
    }
}
