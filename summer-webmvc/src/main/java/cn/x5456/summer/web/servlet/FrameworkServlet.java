package cn.x5456.summer.web.servlet;

import cn.x5456.summer.context.ApplicationContext;
import cn.x5456.summer.context.ApplicationListener;
import cn.x5456.summer.context.event.ContextRefreshedEvent;
import cn.x5456.summer.core.env.Environment;
import cn.x5456.summer.web.context.support.JsonWebApplicationContext;
import cn.x5456.summer.web.context.support.StandardServletEnvironment;
import cn.x5456.summer.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

/**
 * @author yujx
 * @date 2020/04/22 13:16
 */
public abstract class FrameworkServlet extends HttpServletBean {

    /**
     * WebApplicationContext for this servlet
     */
    private WebApplicationContext wac;

    // 是否已经触发刷新事件
    private boolean refreshEventReceived;

    // 放入 ServletContext 中的属性的前缀
    public static final String SERVLET_CONTEXT_PREFIX = FrameworkServlet.class.getName() + ".CONTEXT.";

    /**
     * 子类可以重写此方法以执行自定义初始化。在调用此方法之前，将设置此servlet的所有bean属性。此默认实现不执行任何操作。
     */
    @Override
    protected void initServletBean() {
        this.createWebApplicationContext();
        this.initFrameworkServlet();
    }

    private void createWebApplicationContext() {

        // 获取 servlet 上下文
        ServletContext servletContext = super.getServletContext();

        // 寻找是否具有父容器（Listener 中加载的）
        WebApplicationContext rootContext = (WebApplicationContext) servletContext.getAttribute(WebApplicationContext.WEB_APPLICATION_CONTEXT_ATTRIBUTE_NAME);

        // 如果当前的 web 容器为空，则检查 ServletContext 中有没有
        if (wac == null) {
            wac = (WebApplicationContext) servletContext.getAttribute(this.getServletContextAttributeName());
        }

        // 如果当前的 web 容器为空，则创建一个
        if (wac == null) {
            // SpringMVC 配置文件地址
            String mvcConfigLocation = super.getServletConfig().getInitParameter("mvcConfigLocation");
            String realPath = servletContext.getRealPath(mvcConfigLocation);
            String[] locations = new String[]{realPath};
            wac = new JsonWebApplicationContext(locations, rootContext);
            this.configureAndRefreshWebApplicationContext();
        }

        // 如果还没刷新 DispatchServlet 则进行刷新
        if (!refreshEventReceived) {
            onRefresh(wac);
        }

        // 将当前 web 容器放进 ServletContext 中
        String attrName = this.getServletContextAttributeName();
        servletContext.setAttribute(attrName, wac);
    }

    public String getServletContextAttributeName() {
        return SERVLET_CONTEXT_PREFIX + super.getServletName();
    }

    private void configureAndRefreshWebApplicationContext() {
        wac.setServletContext(super.getServletContext());
        wac.setServletConfig(super.getServletConfig());

        // SpringMVC 中还使用 SourceFilteringListener 包装了一下，为了方便这里就不包装了
        wac.addApplicationListener(new ContextRefreshListener());

        // 获取到 applicationContext 的环境，将 ServletContext 中的参数放进 env 中
        Environment env = wac.getEnvironment();
        if (env instanceof StandardServletEnvironment) {
            ((StandardServletEnvironment) env).initPropertySources(super.getServletContext(), super.getServletConfig());
        }
    }

    // 空实现，在 MVC 中也没有人重写它
    protected void initFrameworkServlet() {
    }

    private class ContextRefreshListener implements ApplicationListener<ContextRefreshedEvent> {

        @Override
        public void onApplicationEvent(ContextRefreshedEvent event) {
            FrameworkServlet.this.onApplicationEvent(event);
        }

        /**
         * 获取事件类型
         */
        @Override
        public Class<ContextRefreshedEvent> getEventType() {
            return ContextRefreshedEvent.class;
        }
    }

    private void onApplicationEvent(ContextRefreshedEvent event) {
        this.refreshEventReceived = true;
        this.onRefresh(event.getApplicationContext());
    }

    /**
     * DispatchServlet 重写了这个方法
     */
    protected void onRefresh(ApplicationContext applicationContext) {
        // For subclasses: do nothing by default.
    }

//    protected abstract void doService(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException;
}
