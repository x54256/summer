package cn.x5456.summer.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface View {

    /**
     * 渲染给定指定模型的视图。
     * <p>
     * 第一步将是准备请求：在JSP情况下，这意味着将模型对象设置为请求属性。
     * 第二步将是视图的实际呈现，例如，通过RequestDispatcher包含JSP。
     *
     * @param model    Map with name Strings as keys and corresponding model
     *                 objects as values (Map can also be {@code null} in case of empty model)
     * @param request  current HTTP request
     * @param response HTTP response we are building
     */
    void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response);

}