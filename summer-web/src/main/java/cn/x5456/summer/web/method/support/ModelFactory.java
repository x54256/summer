package cn.x5456.summer.web.method.support;

import cn.x5456.summer.web.bind.annotation.ModelAttribute;
import cn.x5456.summer.web.request.ServletWebRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * 模型工厂
 */
public final class ModelFactory {

    private final List<InvocableHandlerMethod> modelMethods = new ArrayList<>();

    private final WebDataBinderFactory dataBinderFactory;

    public ModelFactory(List<InvocableHandlerMethod> invocableMethods, WebDataBinderFactory dataBinderFactory) {
        this.modelMethods.addAll(invocableMethods);
        this.dataBinderFactory = dataBinderFactory;
    }

    /**
     * 初始化 Model
     */
    public void initModel(ServletWebRequest webRequest, ModelAndViewContainer mavContainer) {

        for (InvocableHandlerMethod modelMethod : modelMethods) {
            // 获取方法上注解的值
            String modelName = modelMethod.getMethodAnnotation(ModelAttribute.class).value();

            // 判断 mavContainer 中有没有
            if (mavContainer.containsAttribute(modelName)) {
                continue;
            }

            // 没有，则执行方法，放进 mavContainer 中
            Object returnValue = modelMethod.invokeForRequest(webRequest, mavContainer);

            if (!modelMethod.isVoid()) {
                mavContainer.addAttribute(modelName, returnValue);
            }
        }
    }

    /**
     * 更新 Model
     */
    public void updateModel(ServletWebRequest webRequest, ModelAndViewContainer mavContainer) {

    }
}