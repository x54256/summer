package cn.x5456.summer.core;

import java.lang.reflect.Method;

public class MethodParameter {

	// 方法
    private final Method method;

    // 参数对应的索引位置
    private final int parameterIndex;

	public MethodParameter(Method method, int parameterIndex) {
		this.method = method;
		this.parameterIndex = parameterIndex;
	}

	public Class<?> getParameterType() {
		return null;
	}
}