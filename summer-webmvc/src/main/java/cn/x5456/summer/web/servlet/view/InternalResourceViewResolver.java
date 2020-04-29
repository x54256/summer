package cn.x5456.summer.web.servlet.view;

import cn.x5456.summer.web.servlet.View;
import cn.x5456.summer.web.servlet.ViewResolver;

/**
 * @author yujx
 * @date 2020/04/28 17:19
 */
public class InternalResourceViewResolver implements ViewResolver {

    private String prefix = "";

    private String suffix = "";

    /**
     * 通过名称解析给定的视图。
     */
    @Override
    public View resolveViewName(String viewName) {
        InternalResourceView view = new InternalResourceView();
        view.setUrl(prefix + viewName + suffix);

        // 执行 Init#afterPropertiesSet() 方法

        return view;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
