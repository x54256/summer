package cn.x5456.testmvc.controller;

import cn.x5456.summer.context.annotation.Configuration;
import cn.x5456.summer.web.servlet.config.annotation.EnableWebMvc;
import cn.x5456.summer.web.servlet.config.annotation.InterceptorRegistry;
import cn.x5456.summer.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author yujx
 * @date 2020/04/26 13:44
 */
@EnableWebMvc
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new TestInterceptor())
//                .addPathPatterns("/**");
    }
}
