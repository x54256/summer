package cn.x5456.summer.web.context;

import cn.x5456.summer.core.env.Environment;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

public interface WebEnvironment extends Environment {

    /**
     * 从 servletContext 和 servletConfig 取出属性，初始化 env
     */
    void initPropertySources(ServletContext servletContext, ServletConfig servletConfig);

}