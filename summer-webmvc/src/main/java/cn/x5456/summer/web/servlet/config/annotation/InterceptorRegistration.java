package cn.x5456.summer.web.servlet.config.annotation;

import cn.hutool.core.collection.CollectionUtil;
import cn.x5456.summer.web.servlet.HandlerInterceptor;
import cn.x5456.summer.web.servlet.handler.MappedInterceptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author yujx
 * @date 2020/04/26 13:13
 */
public class InterceptorRegistration {

    private final HandlerInterceptor interceptor;

    private final List<String> includePatterns = new ArrayList<>();

    private final List<String> excludePatterns = new ArrayList<>();

    public InterceptorRegistration(HandlerInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    public InterceptorRegistration addPathPatterns(String... patterns) {
        this.includePatterns.addAll(Arrays.asList(patterns));
        return this;
    }

    public InterceptorRegistration excludePathPatterns(String... patterns) {
        this.excludePatterns.addAll(Arrays.asList(patterns));
        return this;
    }

    public HandlerInterceptor getInterceptor() {
        // 如果 includePatterns 和 excludePatterns 都是空的，则直接返回这个拦截器
        if (CollectionUtil.isEmpty(includePatterns) && CollectionUtil.isEmpty(excludePatterns)) {
            return interceptor;
        }

        // Spring 中在这里将其转换成了数组，目的是为了让其不可变
        return new MappedInterceptor(interceptor, includePatterns, excludePatterns);
    }
}
