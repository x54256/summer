package cn.x5456.summer.rpc.config.summer;

import cn.x5456.summer.beans.factory.support.BeanDefinitionRegistry;
import cn.x5456.summer.beans.factory.support.DefaultBeanDefinition;
import cn.x5456.summer.context.annotation.ImportBeanDefinitionRegistrar;
import cn.x5456.summer.core.type.AnnotationMetadata;

/**
 * @author yujx
 * @date 2020/05/16 10:59
 */
public class RpcComponentScanRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        // 注册对 @Service 注解标注的类注册的 BeanDefinitionRegistryPostProcessor
        String basePackage = importingClassMetadata.getAnnotation(RpcComponentScan.class).value();
        DefaultBeanDefinition bdDef = DefaultBeanDefinition.getBD(ServiceAnnotationBeanPostProcessor.class);

        bdDef.addProperty("basePackage", String.class.getName(), basePackage, null);
        registry.registerBeanDefinition("serviceAnnotationBeanPostProcessor", bdDef);

        // 注册对标注着 @Reference 注解的字段注入值的 bean 后置处理器
        DefaultBeanDefinition bdDef2 = DefaultBeanDefinition.getBD(ReferenceAnnotationBeanPostProcessor.class);
        registry.registerBeanDefinition("referenceAnnotationBeanPostProcessor", bdDef2);
    }
}
