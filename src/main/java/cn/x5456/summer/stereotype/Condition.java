package cn.x5456.summer.stereotype;

public interface Condition {

	/**
	 * 结果是否匹配
	 */
	boolean matches(ConditionContext conditionContext, AnnotationMetadata metadata);

}
