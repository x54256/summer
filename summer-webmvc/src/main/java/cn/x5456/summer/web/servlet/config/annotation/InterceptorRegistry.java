package cn.x5456.summer.web.servlet.config.annotation;

import cn.x5456.summer.web.servlet.HandlerInterceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * 拦截器注册中心
 *
 * @author yujx
 * @date 2020/04/26 13:11
 */
public class InterceptorRegistry {

    private final List<InterceptorRegistration> registrations = new ArrayList<>();

    /**
     * 添加拦截器
     */
    public InterceptorRegistration addInterceptor(HandlerInterceptor interceptor) {
        InterceptorRegistration registration = new InterceptorRegistration(interceptor);
        registrations.add(registration);
        return registration;
    }

    /**
     * 返回所有注册的拦截器。
     */
    protected List<HandlerInterceptor> getInterceptors() {
        List<HandlerInterceptor> interceptors = new ArrayList<>();
        for (InterceptorRegistration registration : registrations) {
            interceptors.add(registration.getInterceptor());
        }
        return interceptors ;
    }
}
