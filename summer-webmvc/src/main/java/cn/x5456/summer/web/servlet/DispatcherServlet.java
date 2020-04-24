package cn.x5456.summer.web.servlet;

import cn.x5456.summer.context.ApplicationContext;

/**
 * @author yujx
 * @date 2020/04/22 15:11
 */
public class DispatcherServlet extends FrameworkServlet {

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
//        initMultipartResolver(context);
//        initLocaleResolver(context);
//        initThemeResolver(context);

        // 初始化处理器映射器，通过处理器映射器找到对应的方法进行执行
//        initHandlerMappings(context);
//        initHandlerAdapters(context);
//        initHandlerExceptionResolvers(context);
//        initRequestToViewNameTranslator(context);
//        initViewResolvers(context);
//        initFlashMapManager(context);
    }

    // 循环处理器映射器，调用它的方法，对请求进行解析，找到对应的 【处理链】

}
