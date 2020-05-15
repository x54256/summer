package cn.x5456.summer.web.servlet.handler;

import cn.x5456.summer.web.servlet.HandlerExecutionChain;
import cn.x5456.summer.web.servlet.HandlerInterceptor;
import cn.x5456.summer.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * AbstractHandlerMapping 的作用就是维护拦截器实例
 *
 * @author yujx
 * @date 2020/04/23 11:30
 */
public abstract class AbstractHandlerMapping implements HandlerMapping {

    private List<HandlerInterceptor> adaptedInterceptors = new ArrayList<>();

    /**
     * 返回此请求的处理程序和所有拦截器。可以根据请求URL，会话状态或实现类选择的任何因素进行选择。
     */
    @Override
    public final HandlerExecutionChain getHandler(HttpServletRequest request) {

        // 根据请求获取处理程序
        Object handler = this.getHandlerInternal(request);
        if (handler == null) {
            throw new RuntimeException("没有为当前请求找到处理器！");
        }

        // 如果是实现了 Controller 接口的情况，则从 bf 中取出
        // if (handler instanceof String) {
        //     String handlerName = (String) handler;
        //     handler = getApplicationContext().getBean(handlerName);
        // }

        // 这个地方和拦截器有关
        return this.getHandlerExecutionChain(handler, request);
    }

    private HandlerExecutionChain getHandlerExecutionChain(Object handler, HttpServletRequest request) {

        HandlerExecutionChain handlerExecutionChain = new HandlerExecutionChain(handler);

        for (HandlerInterceptor interceptor : adaptedInterceptors) {
            if (interceptor instanceof MappedInterceptor) {
                String lookupPath = request.getServletPath();
                MappedInterceptor mappedInterceptor = (MappedInterceptor) interceptor;
                if (mappedInterceptor.matches(lookupPath)) {
                    handlerExecutionChain.addInterceptor(mappedInterceptor.getInterceptor());
                }
            } else {
                // 当一个拦截器没有条件的时候会走这里
                handlerExecutionChain.addInterceptor(interceptor);
            }
        }

        return handlerExecutionChain;
    }

    /**
     * 查找给定请求的处理程序，如果未找到特定请求，则返回 null。
     */
    protected abstract Object getHandlerInternal(HttpServletRequest request);

    public void setInterceptors(List<HandlerInterceptor> interceptors) {
        adaptedInterceptors.addAll(interceptors);
    }
}
