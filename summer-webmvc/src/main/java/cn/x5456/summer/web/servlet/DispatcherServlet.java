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
//        initHandlerMappings(context);
//        initHandlerAdapters(context);
//        initHandlerExceptionResolvers(context);
//        initRequestToViewNameTranslator(context);
//        initViewResolvers(context);
//        initFlashMapManager(context);
    }

}
