package cn.x5456.summer.web.servlet.mvc.method.annotation;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.x5456.summer.core.MethodParameter;
import cn.x5456.summer.core.util.JsonUtils;
import cn.x5456.summer.web.bind.annotation.ResponseBody;
import cn.x5456.summer.web.method.support.HandlerMethodReturnValueHandler;
import cn.x5456.summer.web.method.support.ModelAndViewContainer;
import cn.x5456.summer.web.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;

/**
 * 在 Spring 中这个类是负责解析 @RequestBody 和 @ResponseBody 的，我这里只对 @ResponseBody 进行处理
 *
 * @author yujx
 * @date 2020/04/28 11:07
 */
public class RequestResponseBodyMethodProcessor implements HandlerMethodReturnValueHandler {

    /**
     * 此处理程序是否支持给定的{@linkplain MethodParameter 方法返回类型}。
     */
    @Override
    public boolean supportsReturnType(MethodParameter returnType) {

        Method method = returnType.getMethod();
        ResponseBody responseBody = AnnotationUtil.getAnnotation(method, ResponseBody.class);
        if (responseBody != null) {
            return true;
        }

        Class<?> clazz = method.getDeclaringClass();
        ResponseBody classResponseBody = AnnotationUtil.getAnnotation(method, ResponseBody.class);
        if (classResponseBody != null) {
            return true;
        }

        return false;
    }

    /**
     * 通过向模型添加属性并设置视图
     * 或将{@link ModelAndViewContainer＃setRequestHandled}标志设置为{@code true}来处理给定的返回值，以指示已直接处理响应。
     */
    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, ServletWebRequest webRequest) {

        // 表示请求已结束
        mavContainer.setRequestHandled(true);

        // 获取到请求和响应对象，根据请求头等信息往响应中写入数据
        HttpServletRequest request = webRequest.getRequest();
        HttpServletResponse response = webRequest.getResponse();

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        try (Writer writer = response.getWriter()) {
            writer.write(JsonUtils.toString(returnValue));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
