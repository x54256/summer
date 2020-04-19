package cn.x5456.summer.stereotype;

import cn.x5456.summer.beans.factory.BeanFactory;

public interface Condition {

	/**
	 * 结果是否匹配
	 */
	boolean matches(BeanFactory bf, AnnotationMetadata metadata);

}
