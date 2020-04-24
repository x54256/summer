package cn.x5456.summer.web.servlet.handler;

import cn.x5456.summer.web.servlet.HandlerExecutionChain;
import cn.x5456.summer.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yujx
 * @date 2020/04/23 11:30
 */
public abstract class AbstractHandlerMapping implements HandlerMapping {
    /**
     * 返回此请求的处理程序和所有拦截器。可以根据请求URL，会话状态或实现类选择的任何因素进行选择。
     */
    @Override
    public final HandlerExecutionChain getHandler(HttpServletRequest request) {

        // 根据请求获取处理程序
        Object handler = this.getHandlerInternal(request);
        if (handler == null) {
            return null;
        }

//        return this.getHandlerExecutionChain(handler, request);
        return null;
    }

    /**
     * 查找给定请求的处理程序，如果未找到特定请求，则返回 null。
     */
    protected abstract Object getHandlerInternal(HttpServletRequest request);
}
