package cn.x5456.summer.web.servlet.view;

import cn.x5456.summer.web.servlet.View;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author yujx
 * @date 2020/04/28 17:20
 */
public class InternalResourceView implements View {

    private String url;

    /**
     * 渲染给定指定模型的视图。
     * <p>
     * 第一步将是准备请求：在JSP情况下，这意味着将模型对象设置为请求属性。
     * 第二步将是视图的实际呈现，例如，通过RequestDispatcher包含JSP。
     */
    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) {
        // 将 Model 中的数据放进 request 域中
        model.forEach(request::setAttribute);

        // 获取请求分发器
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(url);

        // 进行 jsp 页面的转发
        try {
            requestDispatcher.forward(request, response);
        } catch (ServletException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void setUrl(String url) {
        this.url = url;
    }
}
