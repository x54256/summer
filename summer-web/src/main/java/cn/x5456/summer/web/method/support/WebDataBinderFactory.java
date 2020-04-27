package cn.x5456.summer.web.method.support;

import cn.x5456.summer.web.bind.WebDataBinder;
import cn.x5456.summer.web.request.ServletWebRequest;

public interface WebDataBinderFactory {

    /**
     * 为给定对象创建一个{@link WebDataBinder}。
     */
    WebDataBinder createBinder(ServletWebRequest webRequest, Object target, String objectName);

}