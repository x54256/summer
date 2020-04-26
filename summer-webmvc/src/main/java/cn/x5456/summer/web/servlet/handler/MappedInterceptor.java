package cn.x5456.summer.web.servlet.handler;

import cn.x5456.summer.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Spring 中这里实现了 HandlerInterceptor 是因为 adaptedInterceptors 中有些不是 MappedInterceptor 的实现，而是 HandlerInterceptor
 * <p>
 * 这里可以理解为它采用了装饰者模式，动态的给 HandlerInterceptor 增加了 matches 方法
 * <p>
 * MappedInterceptor 与 InterceptorRegistration 之间是类似于 String 和 StringBuilder 之间的关系
 * <p>
 * MappedInterceptor 也是不可变对象
 *
 * @author yujx
 * @date 2020/04/26 11:10
 */
public final class MappedInterceptor implements HandlerInterceptor {

    private final HandlerInterceptor interceptor;

    private final List<String> includePatterns;

    private final List<String> excludePatterns;

    public MappedInterceptor(HandlerInterceptor interceptor, List<String> includePatterns, List<String> excludePatterns) {
        this.interceptor = interceptor;
        this.includePatterns = includePatterns;
        this.excludePatterns = excludePatterns;
    }

    // 全部 return true 了，不想写逻辑
    public boolean matches(String lookupPath) {
        return true;
    }


    public HandlerInterceptor getInterceptor() {
        return interceptor;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return interceptor.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        interceptor.afterCompletion(request, response, handler, ex);
    }
}
