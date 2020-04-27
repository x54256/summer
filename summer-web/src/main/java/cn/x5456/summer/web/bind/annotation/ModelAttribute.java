package cn.x5456.summer.web.bind.annotation;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ModelAttribute {

	/**
	 * 要绑定到的 Model 属性的名称。
	 */
	String value();

}
