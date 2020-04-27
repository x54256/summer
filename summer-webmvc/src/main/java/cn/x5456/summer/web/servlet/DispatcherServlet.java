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
//        initViewResolvers(context);

        // 6. FlashMap 管理器【可能讲】
//        initFlashMapManager(context);
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

    @Override
    protected void doService(HttpServletRequest request, HttpServletResponse response) {
        // 为请求设置一些属性

        // 调用处理方法
        this.doDispatch(request, response);
    }

    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) {

        // 获取对当前请求处理的处理链路
        HandlerExecutionChain mappedHandler = this.getHandler(request);
        if (mappedHandler == null || mappedHandler.getHandler() == null) {
            return;
        }

        HandlerAdapter ha = this.getHandlerAdapter(mappedHandler.getHandler());
        ModelAndView mv = ha.handle(request, response, mappedHandler.getHandler());



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

    private HandlerAdapter getHandlerAdapter(Object handler) {
        for (HandlerAdapter ha : this.handlerAdapters) {
            if (ha.supports(handler)) {
                return ha;
            }
        }

        throw new RuntimeException("没有找到合适的适配器！");
    }

}
