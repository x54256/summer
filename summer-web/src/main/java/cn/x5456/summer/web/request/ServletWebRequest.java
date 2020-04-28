package cn.x5456.summer.web.request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 为了复用吧，所以封装了一个对象
 */
public class ServletWebRequest {

    private HttpServletRequest request;

    private HttpServletResponse response;

    public ServletWebRequest(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    public String[] getParameterValues(String name) {
        return request.getParameterValues(name);
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }
}