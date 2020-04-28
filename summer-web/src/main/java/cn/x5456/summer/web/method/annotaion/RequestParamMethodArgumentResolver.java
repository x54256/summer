package cn.x5456.summer.web.method.annotaion;

import cn.x5456.summer.core.MethodParameter;
import cn.x5456.summer.web.bind.annotation.RequestParam;
import cn.x5456.summer.web.request.ServletWebRequest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 解析 @RequestParam 注解
 *
 * @author yujx
 * @date 2020/04/27 17:22
 */
public class RequestParamMethodArgumentResolver extends AbstractNamedValueMethodArgumentResolver {

    /**
     * 此解析器是否支持给定的{@linkplain MethodParameter 方法参数}。
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return this.getParameterAnnotation(parameter) != null;
    }

    private Annotation getParameterAnnotation(MethodParameter parameter) {
        // 获取参数上的注解
        Method method = parameter.getMethod();
        int parameterIndex = parameter.getParameterIndex();

        Annotation[] parameterAnnotation = method.getParameterAnnotations()[parameterIndex];
        for (Annotation annotation : parameterAnnotation) {
            if (annotation instanceof RequestParam) {
                return annotation;
            }
        }
        return null;
    }

    @Override
    protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
        // 获取参数上的注解
        RequestParam requestParam = (RequestParam) this.getParameterAnnotation(parameter);
        if (requestParam == null) {
            throw new RuntimeException("参数上没有 @RequestParam 注解");
        }

        return new NamedValueInfo(requestParam.value(), requestParam.required(), requestParam.defaultValue());
    }

    @Override
    protected Object resolveName(String name, MethodParameter parameter, ServletWebRequest webRequest) {
        Object arg = null;
        String[] paramValues = webRequest.getParameterValues(name);
        if (paramValues != null) {
            arg = (paramValues.length == 1 ? paramValues[0] : paramValues);
        }
        return arg;
    }
}
