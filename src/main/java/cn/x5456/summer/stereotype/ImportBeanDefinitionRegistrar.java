package cn.x5456.summer.stereotype;

import cn.x5456.summer.beans.factory.BeanDefinitionRegistry;

public interface ImportBeanDefinitionRegistrar {

	/**
	 * @param importingClassMetadata 被 @Import 注解的类的注解元数据
	 * @param registry bd registry
	 */
	void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry);

}
