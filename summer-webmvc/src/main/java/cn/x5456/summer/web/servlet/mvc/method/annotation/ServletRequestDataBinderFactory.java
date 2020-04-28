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

        // 创建一个 WebDataBinder 对象
        WebDataBinder webDataBinder = new WebDataBinder(target, objectName);

        // 使用 initializer 对 WebDataBinder 进行初始化
        if (initializer != null) {
            initializer.initBinder(webDataBinder, webRequest);
        }

        // 执行用户配置的 initBinder 方法，将自定义绑定器注册到 InitBinderRegister
        for (InvocableHandlerMethod binderMethod : binderMethods) {
            binderMethod.invokeForRequest(webRequest, null, webDataBinder);
        }

        return webDataBinder;
    }
}
