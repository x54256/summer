package cn.x5456.summer.web.servlet.config.annotation;

public interface WebMvcConfigurer {

    default void addInterceptors(InterceptorRegistry registry) {
    }

}