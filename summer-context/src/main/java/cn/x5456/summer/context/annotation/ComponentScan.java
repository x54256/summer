package cn.x5456.summer.context.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ComponentScan {

	/**
	 * 扫描的包
	 */
	String[] value();
}