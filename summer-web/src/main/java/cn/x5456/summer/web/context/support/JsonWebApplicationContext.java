package cn.x5456.summer.web.context.support;

import cn.x5456.summer.context.ApplicationContext;
import cn.x5456.summer.context.support.FileSystemJsonApplicationContext;
import cn.x5456.summer.core.env.Environment;
import cn.x5456.summer.web.context.WebApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

/**
 * @author yujx
 * @date 2020/04/21 17:35
 */
public class JsonWebApplicationContext extends FileSystemJsonApplicationContext implements WebApplicationContext {

    private ServletContext servletContext;

    private ServletConfig servletConfig;

    // 在 SpringMVC 中 spring-mvc.xml 文件的位置是在 setServletContext() 中，从 setServletContext() 中获取到的，然后调用 refresh 方法
    // 此处为了迎合我们的写法（把加载 bd 放到了构造中），所以必须要这样写
    // 注：在 Spring 中加载 bd 是 bf 的方法，new 出 bd 使用者(ApplicationContext)自己调用的。
    public JsonWebApplicationContext(String[] locations) {
        super(locations);
    }

    public JsonWebApplicationContext(String[] locations, ApplicationContext parent) {
        super(locations, parent);
    }

    // -----> WebApplicationContext 中的方法


    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public ServletConfig getServletConfig() {
        return servletConfig;
    }

    @Override
    public void setServletConfig(ServletConfig servletConfig) {
        this.servletConfig = servletConfig;
    }

    @Override
    protected Environment createEnvironment() {
        return new StandardServletEnvironment();
    }
}
