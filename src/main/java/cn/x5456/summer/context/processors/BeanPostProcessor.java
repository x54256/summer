package cn.x5456.summer.context.processors;

/**
 * BeanPostProcessor是spring框架的一个扩展的类点-后置处理器（有很多个）
 * <p>
 * 我们再写代码的过程中可以通过实现BeanPostProcessor这个接口，在Spring对bean进行实例化的过程中，
 * 实现对Bean的一系列操作，从而真正实现插手“她”的人生。进而减轻BeanFactory的负担；
 * 特别注意的一点是这个接口可以实现多个，实现一个列表，然后依次执行；
 * 比如SpringAOP就是通过Spring对bean实例化的后期，对bean进行一些横切逻辑的织入处理的；
 * <p>
 * 常见的内置处理器如下：
 * 0 = {ApplicationContextAwareProcessor@2005}:
 * 1 = {ConfigurationClassPostProcessor$ImportAwareBeanPostProcessor@2006}
 * 2 = {PostProcessorRegistrationDelegate$BeanPostProcessorChecker@2007}
 * 3 = {CommonAnnotationBeanPostProcessor@2008}
 * 4 = {AutowiredAnnotationBeanPostProcessor@2009}
 * 5 = {RequiredAnnotationBeanPostProcessor@2010}
 * 6 = {ApplicationListenerDetector@2011}
 */
public interface BeanPostProcessor {

    /**
     * 在bean的初始化之前执行
     */
    default Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    /**
     * 在bean的初始化后执行
     */
    default Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }

}
