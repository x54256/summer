package cn.x5456.summer.web.servlet.mvc.method.annotation;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.x5456.summer.beans.factory.InitializingBean;
import cn.x5456.summer.context.ApplicationContext;
import cn.x5456.summer.context.ApplicationContextAware;
import cn.x5456.summer.web.bind.annotation.ControllerAdvice;
import cn.x5456.summer.web.method.HandlerMethod;
import cn.x5456.summer.web.method.annotaion.RequestParamMethodArgumentResolver;
import cn.x5456.summer.web.method.support.*;
import cn.x5456.summer.web.request.ServletWebRequest;
import cn.x5456.summer.web.servlet.HandlerExceptionResolver;
import cn.x5456.summer.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author yujx
 * @date 2020/04/29 10:57
 */
public class ExceptionHandlerExceptionResolver implements ApplicationContextAware, InitializingBean, HandlerExceptionResolver {

    private ApplicationContext applicationContext;

    private HandlerMethodArgumentResolverComposite argumentResolvers;

    private HandlerMethodReturnValueHandlerComposite returnValueHandlers;

    private final Map<Object, ExceptionHandlerMethodResolver> exceptionHandlerAdviceCache = new LinkedHashMap<>();

    /**
     * 注入 ApplicationContext
     */
    @Override
    public void setApplicationContext(ApplicationContext ctx) {
        this.applicationContext = ctx;
    }

    /**
     * 设置所有提供的bean属性后，由BeanFactory调用。
     */
    @Override
    public void afterPropertiesSet() {

        // 初始化全局的 @ExceptionHandler
        this.initExceptionHandlerAdviceCache();

        // 初始化参数解析器
        if (this.argumentResolvers == null) {
            List<HandlerMethodArgumentResolver> resolvers = this.getDefaultArgumentResolvers();
            this.argumentResolvers = new HandlerMethodArgumentResolverComposite().addResolvers(resolvers);
        }

        // 初始化返回值解析器
        if (this.returnValueHandlers == null) {
            List<HandlerMethodReturnValueHandler> handlers = this.getDefaultReturnValueHandlers();
            this.returnValueHandlers = new HandlerMethodReturnValueHandlerComposite().addHandlers(handlers);
        }
    }

    private void initExceptionHandlerAdviceCache() {
        // 从容器中寻找标注 @ControllerAdvice 注解的 bean
        List<Object> beans = new ArrayList<>();
        for (Object bean : applicationContext.getBeansOfType(Object.class).values()) {
            if (AnnotationUtil.getAnnotation(bean.getClass(), ControllerAdvice.class) != null) {
                beans.add(bean);
            }
        }

        // 循环 @ControllerAdvice 注解的 bean，在里面找 @ExceptionHandler 加入全局的缓存
        // 我就不找它的父类了
        for (Object bean : beans) {
            ExceptionHandlerMethodResolver resolver = new ExceptionHandlerMethodResolver(bean.getClass());
            if (resolver.hasExceptionMappings()) {
                exceptionHandlerAdviceCache.put(bean, resolver);
            }
        }
    }

    private List<HandlerMethodArgumentResolver> getDefaultArgumentResolvers() {
        return Arrays.asList(
                new RequestParamMethodArgumentResolver()
        );
    }

    private List<HandlerMethodReturnValueHandler> getDefaultReturnValueHandlers() {
        return Arrays.asList(
                new RequestResponseBodyMethodProcessor(),
                new ViewNameMethodReturnValueHandler()
        );
    }

    /**
     * 尝试解决在处理程序执行期间引发的给定异常，如果合适，返回代表特定错误页面的{@link ModelAndView}。
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        // 寻找异常处理器，并创建 ServletInvocableHandlerMethod
        ServletInvocableHandlerMethod exceptionHandlerMethod = this.getExceptionHandlerMethod((HandlerMethod) handler, ex);
        if (exceptionHandlerMethod == null) {
            return null;
        }

        // 设置参数解析器，返回值解析器，执行方法
        exceptionHandlerMethod.setHandlerMethodArgumentResolvers(argumentResolvers);
        exceptionHandlerMethod.setHandlerMethodReturnValueHandlers(returnValueHandlers);

        ModelAndViewContainer mavContainer = new ModelAndViewContainer();
        exceptionHandlerMethod.invokeAndHandle(new ServletWebRequest(request, response), mavContainer);

        // 根据请求是否已完成返回不同的 mav
        if (mavContainer.isRequestHandled()) {
            return new ModelAndView();
        } else {
            // 略
//            ModelAndView mav = new ModelAndView().addAllObjects(mavContainer.getModel());
//            mav.setViewName(mavContainer.getViewName());
//            if (!mavContainer.isViewReference()) {
//                mav.setView((View) mavContainer.getView());
//            }
//            return mav;
        }

        return null;
    }

    public ServletInvocableHandlerMethod getExceptionHandlerMethod(HandlerMethod handlerMethod, Exception ex) {

        // 先在自己类中找
        Class<?> handlerType = handlerMethod.getBeanType();
        ExceptionHandlerMethodResolver resolver = new ExceptionHandlerMethodResolver(handlerType);
        if (resolver.hasExceptionMappings()) {
            Method method = resolver.resolveMethod(ex);
            if (method != null) {
                return new ServletInvocableHandlerMethod(handlerMethod.getBean(), method);
            }
        }

        // 找不到，则去全局缓存中找
        for (Map.Entry<Object, ExceptionHandlerMethodResolver> entry : exceptionHandlerAdviceCache.entrySet()) {
            ExceptionHandlerMethodResolver adviceResolver = entry.getValue();
            Method method = adviceResolver.resolveMethod(ex);
            if (method != null) {
                return new ServletInvocableHandlerMethod(handlerMethod.getBean(), method);
            }
        }

        return null;
    }
}
