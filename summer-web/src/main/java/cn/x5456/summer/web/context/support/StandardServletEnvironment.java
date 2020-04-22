package cn.x5456.summer.web.context.support;

import cn.x5456.summer.core.env.StandardEnvironment;
import cn.x5456.summer.web.context.WebEnvironment;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

/**
 * @author yujx
 * @date 2020/04/22 14:00
 */
public class StandardServletEnvironment extends StandardEnvironment implements WebEnvironment {

    /**
     * 从 servletContext 和 servletConfig 取出属性，初始化 env
     */
    @Override
    public void initPropertySources(ServletContext servletContext, ServletConfig servletConfig) {
        // 我不会写
        // StandardServletEnvironment 中 Spring 给他添加了这些东西 servletContextInitParams、servletConfigInitParams

    }
}
