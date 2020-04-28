package cn.x5456.summer.web.bind.support;

import cn.x5456.summer.web.bind.WebDataBinder;
import cn.x5456.summer.web.request.ServletWebRequest;

/**
 * @author yujx
 * @date 2020/04/27 16:07
 */
public interface WebBindingInitializer {

    /**
     * Initialize the given DataBinder for the given request.
     *
     * @param binder  the DataBinder to initialize
     * @param request the web request that the data binding happens within
     */
    void initBinder(WebDataBinder binder, ServletWebRequest request);
}
