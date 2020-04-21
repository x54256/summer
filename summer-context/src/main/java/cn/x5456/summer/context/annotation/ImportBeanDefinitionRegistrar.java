package cn.x5456.summer.context.annotation;

import cn.x5456.summer.beans.factory.support.BeanDefinitionRegistry;
import cn.x5456.summer.core.type.AnnotationMetadata;

public interface ImportBeanDefinitionRegistrar {

	/**
	 * @param importingClassMetadata 被 @Import 注解的类的注解元数据
	 * @param registry bd registry
	 */
	void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry);

}
