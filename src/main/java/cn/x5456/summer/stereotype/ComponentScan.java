package cn.x5456.summer.stereotype;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ComponentScan {

	/**
	 * 扫描的包
	 */
	String[] value();
}