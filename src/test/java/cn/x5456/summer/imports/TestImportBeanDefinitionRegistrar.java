package cn.x5456.summer.imports;

import cn.x5456.summer.Apple;
import cn.x5456.summer.beans.DefaultBeanDefinition;
import cn.x5456.summer.beans.factory.BeanDefinitionRegistry;
import cn.x5456.summer.stereotype.AnnotationMetadata;
import cn.x5456.summer.stereotype.ImportBeanDefinitionRegistrar;

/**
 * @author yujx
 * @date 2020/04/19 11:31
 */
public class TestImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    /**
     * @param importingClassMetadata 被 @Import 注解的类的注解元数据
     * @param registry               bd registry
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        DefaultBeanDefinition bd = new DefaultBeanDefinition();
        bd.setName("apple");
        bd.setClassName(Apple.class.getName());

        registry.registerBeanDefinition(bd.getName(), bd);
    }
}
