package cn.x5456.summer.web.servlet.mvc.method.annotation;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.x5456.summer.web.bind.annotation.ExceptionHandler;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ExceptionHandlerMethodResolver {

    private final Map<Class<? extends Exception>, Method> mappedMethods = new HashMap<>();

    public ExceptionHandlerMethodResolver(Class<?> handlerType) {
        for (Method method : handlerType.getMethods()) {
            ExceptionHandler exceptionHandler = AnnotationUtil.getAnnotation(method, ExceptionHandler.class);
            if (exceptionHandler != null) {
                Class<? extends Exception>[] exClasses = exceptionHandler.value();
                for (Class<? extends Exception> exClass : exClasses) {
                    mappedMethods.put(exClass, method);
                }
            }
        }
    }

    public boolean hasExceptionMappings() {
        return !mappedMethods.isEmpty();
    }

    public Method resolveMethod(Exception ex) {
        List<Class<? extends Exception>> classList = new ArrayList<>();
        mappedMethods.forEach(((exClass, method) -> {
            if (exClass.isAssignableFrom(ex.getClass())) {
                classList.add(exClass);
            }
        }));

        // 排序，省略


        return mappedMethods.get(classList.get(0));
    }
}