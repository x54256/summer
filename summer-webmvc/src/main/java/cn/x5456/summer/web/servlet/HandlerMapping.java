package cn.x5456.summer.web.servlet;

import javax.servlet.http.HttpServletRequest;

/**
 * 由定义请求和处理程序对象之间的映射关系的对象实现的接口。
 *
 * @author yujx
 * @date 2020/04/23 11:29
 */
public interface HandlerMapping {

    /**
     * 返回此请求的处理程序和所有拦截器。可以根据请求URL，会话状态或实现类选择的任何因素进行选择。
     */
    HandlerExecutionChain getHandler(HttpServletRequest request);
}
