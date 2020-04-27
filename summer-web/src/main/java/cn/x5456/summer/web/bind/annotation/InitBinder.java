package cn.x5456.summer.web.bind.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InitBinder {

	// 这个功能我不做，直接应用 Controller 中的全部方法
//	/**
//	 * 应该使用此init-binder方法的命令/表单属性和/或请求参数的名称。
//	 */
//	String[] value() default {};

}