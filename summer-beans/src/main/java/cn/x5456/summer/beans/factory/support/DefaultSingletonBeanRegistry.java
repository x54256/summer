package cn.x5456.summer.beans.factory.support;

import cn.x5456.summer.beans.factory.DisposableBean;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author yujx
 * @date 2020/04/16 12:28
 */
public class DefaultSingletonBeanRegistry {

    private final Map<String, DisposableBean> disposableBeans = new LinkedHashMap<>();

    /** 注册销毁信息 */
    public void registerDisposableBean(String beanName, DisposableBean bean) {
        this.disposableBeans.put(beanName, bean);
    }

    /** 执行销毁方法 */
    public void destroySingletons() {
        // 调用销毁方法
        for (DisposableBean disposableBean : disposableBeans.values()) {
            disposableBean.destroy();
        }
        // 清空 map
        disposableBeans.clear();
    }
}
