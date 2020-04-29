package cn.x5456.summer.web.servlet;

import cn.x5456.summer.web.ModelMap;

public class ModelAndView {

    /**
     * View instance or view name String
     */
    private Object view;

    /**
     * Model Map
     */
    private ModelMap model;

    public ModelAndView() {
    }

    public ModelAndView(Object view, ModelMap model) {
        this.view = view;
        this.model = model;
    }

    public ModelMap getModel() {
        return model;
    }

    public Object getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public boolean isReference() {
        return (this.view instanceof String);
    }

    public boolean isEmpty() {
        return view == null && model == null;
    }
}