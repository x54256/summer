package cn.x5456.summer.context.annotation;

import cn.x5456.summer.core.type.AnnotationMetadata;

/**
 * 由类型决定的接口，这些类型根据给定的选择标准（通常是一个或多个注释属性）来确定应导入哪个@Configuration类。
 */
public interface ImportSelector {

	/**
	 * @param importingClassMetadata 被 @Import 注解的类的注解元数据
	 */
	String[] selectImports(AnnotationMetadata importingClassMetadata);

}
