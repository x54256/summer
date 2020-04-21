package cn.x5456.summer.context.annotation;

import cn.x5456.summer.core.type.AnnotationMetadata;

public interface Condition {

	/**
	 * 结果是否匹配
	 */
	boolean matches(ConditionContext conditionContext, AnnotationMetadata metadata);

}
