package cn.x5456.summer.web.servlet.mvc.method.annotation;

import cn.x5456.summer.web.bind.WebDataBinder;
import cn.x5456.summer.web.bind.support.WebBindingInitializer;
import cn.x5456.summer.web.method.support.InvocableHandlerMethod;
import cn.x5456.summer.web.method.support.WebDataBinderFactory;
import cn.x5456.summer.web.request.ServletWebRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yujx
 * @date 2020/04/27 16:04
 */
public class ServletRequestDataBinderFactory implements WebDataBinderFactory {

    private final List<InvocableHandlerMethod> binderMethods = new ArrayList<>();

    private final WebBindingInitializer initializer;

    public ServletRequestDataBinderFactory(List<InvocableHandlerMethod> binderMethods, WebBindingInitializer initializer) {
        this.binderMethods.addAll(binderMethods);
        this.initializer = initializer;
    }

    /**
     * 为给定对象创建一个{@link WebDataBinder}。
     */
    @Override
    public WebDataBinder createBinder(ServletWebRequest webRequest, Object target, String objectName) {
        return null;
    }
}
