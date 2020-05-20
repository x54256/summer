package cn.x5456.summer.rpc.config.summer;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.x5456.summer.beans.factory.config.BeanDefinitionRegistryPostProcessor;
import cn.x5456.summer.beans.factory.support.BeanDefinitionRegistry;
import cn.x5456.summer.beans.factory.support.DefaultBeanDefinition;
import cn.x5456.summer.rpc.config.ServiceBean;

import java.util.Set;

/**
 * @author yujx
 * @date 2020/05/16 11:03
 */
public class ServiceAnnotationBeanPostProcessor implements BeanDefinitionRegistryPostProcessor {

    // 因为我的 DI 不支持数组和集合，所以这里就不传数组了
    private String basePackage;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
        Set<Class<?>> classes = ClassUtil.scanPackageByAnnotation(basePackage, Service.class);
        for (Class<?> clazz : classes) {
            // 判断是否具有 @Service 注解，并且本身不是注解
            Service service = AnnotationUtil.getAnnotation(clazz, Service.class);
            if (ObjectUtil.isNotNull(service) && !clazz.isAnnotation()) {

                // 获取 clazz 的接口们，我就不校验了
                Class<?>[] interfaces = clazz.getInterfaces();

                // 将标注着 @Service 注解的类注册到容器中
                DefaultBeanDefinition bd = new DefaultBeanDefinition();

                bd.setName(StrUtil.lowerFirst(interfaces[0].getSimpleName()));
                bd.setClassName(clazz.getName());

                registry.registerBeanDefinition(bd.getName(), bd);

                // 构建 ServiceBean 的 bd
                DefaultBeanDefinition serviceBeanBD = new DefaultBeanDefinition();
                serviceBeanBD.setClassName(ServiceBean.class.getName());
                serviceBeanBD.setName("serviceBean:" + bd.getName());
                serviceBeanBD
                        .addProperty("interfaceClass", String.class.getName(), interfaces[0].getName(), null)
                        .addProperty("ref", bd.getClassName(), null, bd.getName());

                // 注册到 Spring 中
                registry.registerBeanDefinition(serviceBeanBD.getName(), serviceBeanBD);
            }
        }
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }
}
