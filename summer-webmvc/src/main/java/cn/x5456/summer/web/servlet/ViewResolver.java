package cn.x5456.summer.web.servlet;

public interface ViewResolver {

    /**
     * 通过名称解析给定的视图。
     * <p>
     * Note: To allow for ViewResolver chaining, a ViewResolver should
     * return {@code null} if a view with the given name is not defined in it.
     * However, this is not required: Some ViewResolvers will always attempt
     * to build View objects with the given name, unable to return {@code null}
     * (rather throwing an exception when View creation failed).
     *
     * @param viewName name of the view to resolve
     *                 ViewResolvers that support internationalization should respect this.
     * @return the View object, or {@code null} if not found
     * (optional, to allow for ViewResolver chaining)
     */
    View resolveViewName(String viewName);

}