package cn.x5456.summer.web.servlet;

import cn.x5456.summer.context.ApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yujx
 * @date 2020/04/22 15:11
 */
public class DispatcherServlet extends FrameworkServlet {

    // 处理器映射器集合
    private List<HandlerMapping> handlerMappings;

    // 处理器适配器集合
    private List<HandlerAdapter> handlerAdapters;

    // 视图解析器集合
    private List<ViewResolver> viewResolvers;

    /**
     * This implementation calls {@link #initStrategies}.
     */
    @Override
    protected void onRefresh(ApplicationContext context) {
        initStrategies(context);
    }

    /**
     * 初始化此servlet使用的策略对象。
     */
    protected void initStrategies(ApplicationContext context) {

        // 5. 文件上传解析器
//        initMultipartResolver(context);
//        initLocaleResolver(context);
//        initThemeResolver(context);

        // 1. 初始化处理器映射器，通过处理器映射器找到对应的方法进行执行
        this.initHandlerMappings(context);

        // 2. 处理器适配器
        this.initHandlerAdapters(context);

        // 3. 异常解析器
//        initHandlerExceptionResolvers(context);
//        initRequestToViewNameTranslator(context);

        // 4. 视图解析器
        this.initViewResolvers(context);
    }

    private void initHandlerMappings(ApplicationContext context) {
        String[] bdNames = context.getBeanDefinitionNames(HandlerMapping.class);
        this.handlerMappings = Arrays.stream(bdNames)
                .map(beanName -> context.getBean(beanName, HandlerMapping.class))
                .collect(Collectors.toList());
    }

    private void initHandlerAdapters(ApplicationContext context) {
        String[] bdNames = context.getBeanDefinitionNames(HandlerAdapter.class);
        this.handlerAdapters = Arrays.stream(bdNames)
                .map(beanName -> context.getBean(beanName, HandlerAdapter.class))
                .collect(Collectors.toList());
    }

    private void initViewResolvers(ApplicationContext context) {
        String[] bdNames = context.getBeanDefinitionNames(ViewResolver.class);
        this.viewResolvers = Arrays.stream(bdNames)
                .map(beanName -> context.getBean(beanName, ViewResolver.class))
                .collect(Collectors.toList());
    }

    @Override
    protected void doService(HttpServletRequest request, HttpServletResponse response) {
        // 为请求设置一些属性

        // 调用处理方法
        this.doDispatch(request, response);
    }

    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) {

        Exception dispatchException = null;

        // 获取对当前请求处理的处理链路
        HandlerExecutionChain mappedHandler = this.getHandler(request);
        if (mappedHandler == null || mappedHandler.getHandler() == null) {
            return;
        }

        // 执行拦截器的请求处理执行前的方法
        if (!mappedHandler.applyPreHandle(request, response)) {
            return;
        }

        // 根据请求处理器获取处理器适配器
        HandlerAdapter ha = this.getHandlerAdapter(mappedHandler.getHandler());

        // 执行，返回 mav
        ModelAndView mv = ha.handle(request, response, mappedHandler.getHandler());
        if (mv == null) {
            return;
        }

        // 执行拦截器的后置处理
        mappedHandler.applyPostHandle(request, response, mv);


        this.processDispatchResult(request, response, mappedHandler, mv, dispatchException);
    }

    private void processDispatchResult(HttpServletRequest request, HttpServletResponse response, HandlerExecutionChain mappedHandler, ModelAndView mv, Exception dispatchException) {

        // 处理异常

        // 渲染视图
        if (mv != null) {
            this.render(mv, request, response);
        }

        // 执行拦截器的最后一个方法
        mappedHandler.triggerAfterCompletion(request, response, null);
    }

    private void render(ModelAndView mv, HttpServletRequest request, HttpServletResponse response) {
        View view;
        if (mv.isReference()) {
            // We need to resolve the view name.
            view = this.resolveViewName((String) mv.getView());
            if (view == null) {
                throw new RuntimeException("xxxx");
            }
        } else {
            view = (View) mv.getView();
        }

        view.render(mv.getModel(), request, response);
    }

    protected View resolveViewName(String viewName) {

        for (ViewResolver viewResolver : this.viewResolvers) {
            View view = viewResolver.resolveViewName(viewName);
            if (view != null) {
                return view;
            }
        }
        return null;
    }

    /**
     * 根据请求获取【处理链】
     */
    private HandlerExecutionChain getHandler(HttpServletRequest request) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            HandlerExecutionChain handler = handlerMapping.getHandler(request);
            if (handler != null) {
                return handler;
            }
        }

        return null;
    }

    /**
     * 根据处理器寻找合适的适配器
     */
    private HandlerAdapter getHandlerAdapter(Object handler) {
        for (HandlerAdapter ha : this.handlerAdapters) {
            if (ha.supports(handler)) {
                return ha;
            }
        }

        throw new RuntimeException("没有找到合适的适配器！");
    }

}
