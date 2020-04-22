package cn.x5456.summer.web.context;

import cn.x5456.summer.context.ApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

/**
 * 部分配置在 Spring 中采用 ConfigurableWebApplicationContext 接口封装
 */
public interface WebApplicationContext extends ApplicationContext {

    /**
     * 成功启动时将 Root WebApplicationContext 绑定到 ServletContext 中的属性。
     */
    String WEB_APPLICATION_CONTEXT_ATTRIBUTE_NAME = WebApplicationContext.class.getName() + ".ROOT";

    void setServletContext(ServletContext servletContext);

    ServletContext getServletContext();

    void setServletConfig(ServletConfig servletConfig);

    ServletConfig getServletConfig();
}