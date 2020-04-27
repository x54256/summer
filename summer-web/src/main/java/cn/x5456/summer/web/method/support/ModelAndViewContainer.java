package cn.x5456.summer.web.method.support;

import cn.x5456.summer.web.BindingAwareModelMap;
import cn.x5456.summer.web.ModelMap;

public class ModelAndViewContainer {

    // 是否结束请求
    private boolean requestHandled = false;

    private final ModelMap defaultModel = new BindingAwareModelMap();

    private Object view;

    public boolean containsAttribute(String attrName) {
        return defaultModel.containsKey(attrName);
    }

    public void addAttribute(String modelName, Object returnValue) {
        defaultModel.put(modelName, returnValue);
    }

    public void setRequestHandled(boolean requestHandled) {
        this.requestHandled = requestHandled;
    }

    public boolean isRequestHandled() {
        return requestHandled;
    }
}