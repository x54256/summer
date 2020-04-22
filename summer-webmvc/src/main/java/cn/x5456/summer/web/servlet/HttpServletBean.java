package cn.x5456.summer.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * @author yujx
 * @date 2020/04/21 15:14
 */
public class HttpServletBean extends HttpServlet {

    /**
     * 当第一次请求到来的时候会调用
     */
    @Override
    public void init() throws ServletException {

        // 从 ServletContext 取出必须的参数设置到当前对象，因为我们没有，就不写了

        // 调用子类的初始化方法
        this.initServletBean();
    }

    /**
     * 子类可以重写此方法以执行自定义初始化。在调用此方法之前，将设置此servlet的所有bean属性。此默认实现不执行任何操作。
     */
    protected void initServletBean() {

    }
}
