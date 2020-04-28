package cn.x5456.summer.web.servlet.config.annotation;

import cn.x5456.summer.beans.factory.InitializingBean;
import cn.x5456.summer.context.ApplicationContext;
import cn.x5456.summer.context.ApplicationContextAware;
import cn.x5456.summer.context.annotation.Bean;
import cn.x5456.summer.web.servlet.HandlerInterceptor;
import cn.x5456.summer.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import cn.x5456.summer.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Collection;
import java.util.List;

/**
 * 为什么要使用 WebMvcConfigurerComposite 作为 WebMvcConfigurer 集合的容器，并且采用组合模式？
 * <p>
 * 可能是为了更面向对象一点吧
 *
 * @author yujx
 * @date 2020/04/26 13:25
 */
public class DelegatingWebMvcConfiguration implements ApplicationContextAware, InitializingBean {

    private ApplicationContext applicationContext;

    private final WebMvcConfigurerComposite configurers = new WebMvcConfigurerComposite();

    @Bean
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        RequestMappingHandlerMapping handlerMapping = new RequestMappingHandlerMapping();
        handlerMapping.setInterceptors(getInterceptors());

        return handlerMapping;
    }

    private List<HandlerInterceptor> getInterceptors() {
        InterceptorRegistry registry = new InterceptorRegistry();
        configurers.addInterceptors(registry);

        return registry.getInterceptors();
    }

    @Bean
    public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
        return new RequestMappingHandlerAdapter();
    }


    @Override
    public void setApplicationContext(ApplicationContext ctx) {
        this.applicationContext = ctx;
    }

    // 当这个对象初始化完成后调用
    @Override
    public void afterPropertiesSet() {
        Collection<WebMvcConfigurer> configurers = applicationContext.getBeansOfType(WebMvcConfigurer.class).values();
        this.configurers.addWebMvcConfigurers(configurers);
    }
}
