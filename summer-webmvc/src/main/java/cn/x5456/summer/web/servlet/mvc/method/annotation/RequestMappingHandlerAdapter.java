package cn.x5456.summer.web.servlet.mvc.method.annotation;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.x5456.summer.beans.factory.InitializingBean;
import cn.x5456.summer.context.ApplicationContext;
import cn.x5456.summer.context.ApplicationContextAware;
import cn.x5456.summer.web.bind.annotation.ControllerAdvice;
import cn.x5456.summer.web.bind.annotation.InitBinder;
import cn.x5456.summer.web.bind.annotation.ModelAttribute;
import cn.x5456.summer.web.bind.support.WebBindingInitializer;
import cn.x5456.summer.web.method.HandlerMethod;
import cn.x5456.summer.web.method.annotaion.RequestParamMethodArgumentResolver;
import cn.x5456.summer.web.method.support.*;
import cn.x5456.summer.web.request.ServletWebRequest;
import cn.x5456.summer.web.servlet.HandlerAdapter;
import cn.x5456.summer.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.*;

public class RequestMappingHandlerAdapter implements ApplicationContextAware, InitializingBean, HandlerAdapter {

    private ApplicationContext applicationContext;

    // 全局 initBinder 的缓存
    private final Map<Object, Set<Method>> initBinderAdviceCache = new LinkedHashMap<>();

    // 全局 modelAttribute 的缓存
    private final Map<Object, Set<Method>> modelAttributeAdviceCache = new LinkedHashMap<>();

    private HandlerMethodArgumentResolverComposite argumentResolvers;

    private HandlerMethodArgumentResolverComposite initBinderArgumentResolvers;

    private HandlerMethodReturnValueHandlerComposite returnValueHandlers;

    // Binder 的初始化器，由配置类注入
    private WebBindingInitializer webBindingInitializer;

    /**
     * 注入 ApplicationContext
     */
    @Override
    public void setApplicationContext(ApplicationContext ctx) {
        this.applicationContext = ctx;
    }

    /**
     * 初始化之后执行
     */
    @Override
    public void afterPropertiesSet() {
        // 初始化全局的 @InitBinder 和 @ModelAttribute
        this.initControllerAdviceCache();

        // 初始化参数解析器
        if (this.argumentResolvers == null) {
            List<HandlerMethodArgumentResolver> resolvers = this.getDefaultArgumentResolvers();
            this.argumentResolvers = new HandlerMethodArgumentResolverComposite().addResolvers(resolvers);
        }

        // 初始化 initBinder 的参数解析器
        if (this.initBinderArgumentResolvers == null) {
            List<HandlerMethodArgumentResolver> resolvers = getDefaultInitBinderArgumentResolvers();
            this.initBinderArgumentResolvers = new HandlerMethodArgumentResolverComposite().addResolvers(resolvers);
        }

        // 初始化返回值解析器
        if (this.returnValueHandlers == null) {
            List<HandlerMethodReturnValueHandler> handlers = getDefaultReturnValueHandlers();
            this.returnValueHandlers = new HandlerMethodReturnValueHandlerComposite().addHandlers(handlers);
        }

    }

    /**
     * 添加默认的参数解析器
     */
    private List<HandlerMethodArgumentResolver> getDefaultArgumentResolvers() {
        return Arrays.asList(
                new RequestParamMethodArgumentResolver()
        );
    }

    /**
     * 添加解析 InitBinder 参数的默认解析器
     */
    private List<HandlerMethodArgumentResolver> getDefaultInitBinderArgumentResolvers() {
        return Arrays.asList(

        );
    }

    /**
     * 添加返回值处理器
     */
    private List<HandlerMethodReturnValueHandler> getDefaultReturnValueHandlers() {
        return Arrays.asList(
                new RequestResponseBodyMethodProcessor()
        );
    }

    private void initControllerAdviceCache() {
        // 从容器中寻找标注 @ControllerAdvice 注解的 bean
        List<Object> beans = new ArrayList<>();
        for (Object bean : applicationContext.getBeansOfType(Object.class).values()) {
            if (AnnotationUtil.getAnnotation(bean.getClass(), ControllerAdvice.class) != null) {
                beans.add(bean);
            }
        }

        // 循环 @ControllerAdvice 注解的 bean，在里面找 @InitBinder 和 @ModelAttribute 加入全局的缓存
        // 我就不找它的父类了
        for (Object bean : beans) {
            Set<Method> initBinderMethods = new HashSet<>();
            Set<Method> modelAttrMethods = new HashSet<>();

            for (Method method : bean.getClass().getMethods()) {
                if (AnnotationUtil.getAnnotation(method, InitBinder.class) != null) {
                    initBinderMethods.add(method);
                }

                // 在 Spring 中如果它携带 @RequestMapping 注解要忽略，我这就不做了
                if (AnnotationUtil.getAnnotation(method, ModelAttribute.class) != null) {
                    modelAttrMethods.add(method);
                }

                initBinderAdviceCache.put(bean, initBinderMethods);
                modelAttributeAdviceCache.put(bean, modelAttrMethods);
            }
        }
    }


    /**
     * 给定处理程序实例，请返回此 HandlerAdapter 是否可以支持它。
     */
    @Override
    public boolean supports(Object handler) {
        return handler instanceof HandlerMethod;
    }

    /**
     * 使用给定的处理程序来处理此请求
     */
    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        return this.invokeHandlerMethod(request, response, handlerMethod);
    }

    private ModelAndView invokeHandlerMethod(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) {

        // 初始化 ServletWebRequest
        ServletWebRequest webRequest = new ServletWebRequest(request, response);

        // 创建数据绑定工厂
        WebDataBinderFactory binderFactory = this.getDataBinderFactory(handlerMethod);

        // 创建 Model 工厂（请求处理前对 Model 进行初始化，请求执行完成后更新 Model 中数据）
        ModelFactory modelFactory = this.getModelFactory(handlerMethod, binderFactory);

        // 创建真正要执行的方法，并注入
        ServletInvocableHandlerMethod invocableMethod = new ServletInvocableHandlerMethod(handlerMethod);
        invocableMethod.setHandlerMethodArgumentResolvers(this.argumentResolvers);
        invocableMethod.setHandlerMethodReturnValueHandlers(this.returnValueHandlers);
        invocableMethod.setDataBinderFactory(binderFactory);
        // invocableMethod.setParameterNameDiscoverer(this.parameterNameDiscoverer);

        // 创建 mavContainer
        ModelAndViewContainer mavContainer = new ModelAndViewContainer();

        // 对 Model 进行初始化，执行用户自定义的 @ModelAttribute 方法，将返回值加入 mavContainer 中
        modelFactory.initModel(webRequest, mavContainer);

        // 执行方法
        invocableMethod.invokeAndHandle(webRequest, mavContainer);

        // 获取到 ModelAndView （将 MavContainer 转换成 MAV），如果是 null，则表示是 @ResponseBody 那种情况
        return this.getModelAndView(mavContainer, modelFactory, webRequest);
    }

    /**
     * 创建数据绑定工厂
     */
    private WebDataBinderFactory getDataBinderFactory(HandlerMethod handlerMethod) {

        // 找到用户定义的数据绑定器，留着在之后数据绑定时使用
        List<InvocableHandlerMethod> initBinderMethods = new ArrayList<>();

        // 先遍历全局的
        initBinderAdviceCache.forEach((bean, methods) -> {
            for (Method method : methods) {
                initBinderMethods.add(this.createInitBinderMethod(bean, method));
            }
        });

        // 再遍历当前 Controller 类的（不管父类）
        Class<?> beanType = handlerMethod.getBeanType();
        for (Method method : beanType.getMethods()) {
            if (AnnotationUtil.getAnnotation(method, InitBinder.class) != null) {
                initBinderMethods.add(this.createInitBinderMethod(handlerMethod.getBean(), method));
            }
        }

        return new ServletRequestDataBinderFactory(initBinderMethods, webBindingInitializer);
    }

    private InvocableHandlerMethod createInitBinderMethod(Object bean, Method method) {
        InvocableHandlerMethod binderMethod = new InvocableHandlerMethod(bean, method);

        // 设置 @initBinder 的参数解析器
        binderMethod.setHandlerMethodArgumentResolvers(this.initBinderArgumentResolvers);

        // 这个不知道干啥的
        // binderMethod.setDataBinderFactory(new DefaultDataBinderFactory(this.webBindingInitializer));

        // 参数名解析器，用到再说吧
        // binderMethod.setParameterNameDiscoverer(this.parameterNameDiscoverer);

        return binderMethod;
    }

    /**
     * 创建 Model 工厂
     */
    private ModelFactory getModelFactory(HandlerMethod handlerMethod, WebDataBinderFactory binderFactory) {

        // 遍历标注了 @ModelAttr 注解的方法，因为要在请求前将它的返回值放进 Model 中
        List<InvocableHandlerMethod> attrMethods = new ArrayList<>();

        // 先遍历全局的
        modelAttributeAdviceCache.forEach((bean, methods) -> {
            for (Method method : methods) {
                attrMethods.add(this.createModelAttributeMethod(binderFactory, bean, method));
            }
        });

        // 再遍历当前 Controller 类的（不管父类）
        Class<?> beanType = handlerMethod.getBeanType();
        for (Method method : beanType.getMethods()) {
            if (AnnotationUtil.getAnnotation(method, ModelAttribute.class) != null) {
                attrMethods.add(this.createModelAttributeMethod(binderFactory, handlerMethod.getBean(), method));
            }
        }

        return new ModelFactory(attrMethods, binderFactory);
    }

    private InvocableHandlerMethod createModelAttributeMethod(WebDataBinderFactory factory, Object bean, Method method) {
        InvocableHandlerMethod attrMethod = new InvocableHandlerMethod(bean, method);

        // 注入参数绑定器
        attrMethod.setHandlerMethodArgumentResolvers(this.argumentResolvers);

        // 参数名解析器
        // attrMethod.setParameterNameDiscoverer(this.parameterNameDiscoverer);

        // 数据绑定工厂
        attrMethod.setDataBinderFactory(factory);

        return attrMethod;
    }

    /**
     * 获取到 ModelAndView
     */
    private ModelAndView getModelAndView(ModelAndViewContainer mavContainer, ModelFactory modelFactory, ServletWebRequest webRequest) {
        if (mavContainer.isRequestHandled()) {
            return null;
        }

        // TODO: 2020/4/27
        return null;
    }
}
