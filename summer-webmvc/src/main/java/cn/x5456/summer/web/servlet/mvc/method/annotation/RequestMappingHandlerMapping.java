package cn.x5456.summer.web.servlet.mvc.method.annotation;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.x5456.summer.beans.factory.InitializingBean;
import cn.x5456.summer.context.ApplicationContext;
import cn.x5456.summer.context.ApplicationContextAware;
import cn.x5456.summer.stereotype.Controller;
import cn.x5456.summer.web.bind.annotation.RequestMapping;
import cn.x5456.summer.web.method.HandlerMethod;
import cn.x5456.summer.web.servlet.handler.AbstractHandlerMapping;
import cn.x5456.summer.web.servlet.mvc.method.RequestMappingInfo;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author yujx
 * @date 2020/04/24 09:33
 */
public class RequestMappingHandlerMapping extends AbstractHandlerMapping implements InitializingBean, ApplicationContextAware {

    private ApplicationContext applicationContext;

    // 映射的注册中心
    private final MappingRegistry mappingRegistry = new MappingRegistry();

    /**
     * 注入 ApplicationContext
     */
    @Override
    public void setApplicationContext(ApplicationContext ctx) {
        this.applicationContext = ctx;
    }

    class MappingRegistry {

        private final Map<RequestMappingInfo, HandlerMethod> mappingLookup = new LinkedHashMap<>();

        private final Multimap<String, RequestMappingInfo> urlLookup = ArrayListMultimap.create();

        public void register(String beanName, Class<?> beanType, Method method, RequestMappingInfo requestMappingInfo) {

            // 创建方法对象
            HandlerMethod handlerMethod = new HandlerMethod(beanName, beanType, applicationContext, method);

            // 添加到 mappingLookup 中
            mappingLookup.put(requestMappingInfo, handlerMethod);

            // 将路径匹配放到 urlLookup 中
            for (String pattern : requestMappingInfo.getPatternsCondition().getPatterns()) {
                urlLookup.put(pattern, requestMappingInfo);
            }
        }

        /**
         * 一个 url 对应多个 RequestMappingInfo 的情况一般出现在请求方式不同的情况下
         */
        public List<RequestMappingInfo> getMappingsByUrl(String urlPath) {
            return new ArrayList<>(this.urlLookup.get(urlPath));
        }

        public Map<RequestMappingInfo, HandlerMethod> getMappings() {
            return mappingLookup;
        }
    }

    /**
     * 设置所有提供的bean属性后，由BeanFactory调用。
     */
    @Override
    public void afterPropertiesSet() {

        Map<String, Object> beansOfType = applicationContext.getBeansOfType(Object.class);
        beansOfType.forEach((bdName, bean) -> {

            Class<?> beanType = bean.getClass();

            // 判断这个类上是否包含 @Controller 注解或者 @RequestMapping 注解
            if (this.isHandler(beanType)) {
                // 如果包含，则解析它方法上的 @RequestMapping 注解，进行注册
                this.detectHandlerMethods(bdName, beanType);
            }
        });
    }

    private boolean isHandler(Class<?> clazz) {
        Controller controller = AnnotationUtil.getAnnotation(clazz, Controller.class);
        if (ObjectUtil.isNotNull(controller)) {
            return true;
        }

        RequestMapping requestMapping = AnnotationUtil.getAnnotation(clazz, RequestMapping.class);
        if (ObjectUtil.isNotNull(requestMapping)) {
            return true;
        }

        return false;
    }

    /**
     * 找到标注 @RequestMapping 的方法，注册到注册中心
     */
    private void detectHandlerMethods(String beanName, Class<?> beanType) {

        // 使用 Map 装的目的是为了去重
        Map<Method, RequestMappingInfo> methodMap = new LinkedHashMap<Method, RequestMappingInfo>();
        Queue<Class<?>> handlerTypes = new LinkedList<>();

        handlerTypes.add(beanType);

        while (!handlerTypes.isEmpty()) {
            Class<?> handlerType = handlerTypes.remove();

            // 将他的父类或者接口
            handlerTypes.addAll(Arrays.asList(handlerType.getInterfaces()));
            Class<?> superclass = handlerType.getSuperclass();
            if (ObjectUtil.isNotNull(superclass)) {
                handlerTypes.add(superclass);
            }

            for (Method method : handlerType.getMethods()) {
                // 判断当前 method 是否有 @RequestMapping 注解，如果有返回 RequestMappingInfo，没有返回 null
                RequestMappingInfo info = this.createRequestMappingInfo(method);
                if (ObjectUtil.isNotNull(info)) {
                    RequestMappingInfo classRequestMappingInfo = this.createRequestMappingInfo(handlerType);
                    if (ObjectUtil.isNotNull(classRequestMappingInfo)) {
                        // 使两者结合
                        info = info.combine(classRequestMappingInfo);
                    }
                }

                // 放进 Map 中
                methodMap.put(method, info);
            }
        }

        methodMap.forEach(((method, info) -> {
            // 注册到映射注册中心
            mappingRegistry.register(beanName, beanType, method, info);
        }));
    }

    private RequestMappingInfo createRequestMappingInfo(AnnotatedElement element) {
        RequestMapping requestMapping = AnnotationUtil.getAnnotation(element, RequestMapping.class);
        if (ObjectUtil.isNotNull(requestMapping)) {
            return null;
        }

        return RequestMappingInfo
                .paths(requestMapping.value())
                .methods(requestMapping.method())
                .build();
    }

    /**
     * 查找给定请求的处理程序，如果未找到特定请求，则返回 null。
     */
    @Override
    protected HandlerMethod getHandlerInternal(HttpServletRequest request) {
//        List<Match> matches = new ArrayList<>();
//
//        String lookupPath = request.getRequestURI();
//        Collection<RequestMappingInfo> directPathMatches = this.mappingRegistry.getMappingsByUrl(lookupPath);
//        // 如果找到匹配的了，就进行校验，找到最合适的
//        if (directPathMatches != null) {
//            this.addMatchingMappings(directPathMatches, matches, request);
//        }
//        // 如果没有找到合适的，说明可能是 @PathVariable 的情况，则把所有的 RequestMappingInfo 交给它进行寻找匹配的。
//        if (matches.isEmpty()) {
//            this.addMatchingMappings(this.mappingRegistry.getMappings().keySet(), matches, request);
//        }
//
//        // 如果匹配的不为空，则取出第一个将它的 handlerMethod 进行返回
//        if (CollUtil.isNotEmpty(matches)) {
//            Match bestMatch = matches.get(0);
//            return bestMatch.handlerMethod;
//        }

        throw new RuntimeException("没有为当前请求找到处理器！");
    }
//
//    /**
//     * 对请求头、请求方法、请求路径（包括 @PathVariable）、请求参数进行校验
//     * <p>
//     * 注：头我就不校验了。
//     */
//    // TODO: 2020/4/24
//    private void addMatchingMappings(Collection<RequestMappingInfo> mappingInfos, List<Match> matches, HttpServletRequest request) {
//        for (RequestMappingInfo mappingInfo : mappingInfos) {
//            boolean isMatch = mappingInfo.getMatchingCondition(request);
//            if (isMatch) {
//                matches.add(new Match(mappingInfo, this.mappingRegistry.getMappings().get(mappingInfo)));
//            }
//        }
//    }
//
//    private class Match {
//
//        private final RequestMappingInfo mapping;
//
//        private final HandlerMethod handlerMethod;
//
//        public Match(RequestMappingInfo mapping, HandlerMethod handlerMethod) {
//            this.mapping = mapping;
//            this.handlerMethod = handlerMethod;
//        }
//
//        @Override
//        public String toString() {
//            return this.mapping.toString();
//        }
//    }
}
