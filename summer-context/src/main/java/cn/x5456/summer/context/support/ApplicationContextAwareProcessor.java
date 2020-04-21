package cn.x5456.summer.context.support;

import cn.x5456.summer.beans.factory.Aware;
import cn.x5456.summer.beans.factory.config.BeanPostProcessor;
import cn.x5456.summer.context.ApplicationContext;
import cn.x5456.summer.context.ApplicationContextAware;
import cn.x5456.summer.context.EnvironmentAware;

/**
 * 与 ApplicationContext 中相关数据注入的后置处理器
 * <p>
 * 在 bean 初始化完成后注入相关实例
 *
 * @author yujx
 * @date 2020/04/17 14:34
 */
public class ApplicationContextAwareProcessor implements BeanPostProcessor {

    private ApplicationContext applicationContext;

    public ApplicationContextAwareProcessor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 在bean的初始化之前执行
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if (bean instanceof Aware) {
            if (bean instanceof ApplicationContextAware) {
                ((ApplicationContextAware) bean).setApplicationContext(applicationContext);
            }
            if (bean instanceof EnvironmentAware) {
                ((EnvironmentAware) bean).setEnvironment(applicationContext.getEnvironment());
            }
            // else if () ...
        }
        return bean;
    }
}
