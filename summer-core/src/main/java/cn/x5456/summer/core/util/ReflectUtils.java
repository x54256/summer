package cn.x5456.summer.core.util;

import cn.hutool.core.util.ArrayUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yujx
 * @date 2020/04/16 12:38
 */
public final class ReflectUtils {

    public static List<Method> getMethodsByAnnotation(Class<?> tClass, Class<? extends Annotation> annotationClass) {
        Method[] methods = tClass.getMethods();

        List<Method> matches = new ArrayList<>();
        if (!ArrayUtil.isEmpty(methods)) {
            for (Method method : methods) {
                if (method.isAnnotationPresent(annotationClass)) {
                    matches.add(method);
                }
            }
        }
        return matches;
    }

    public static <T> T string2BasicType(String val, Class<T> type) {
        if (val == null) {
            return null;
        }
        try {
            Constructor<T> constructor = type.getConstructor(String.class);
            constructor.setAccessible(true);
            return constructor.newInstance(val);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static Class<?> getType(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object invokeMethod(Object obj, String methodName, Class<?>[] parameterTypes, Object[] arguments) {
        try {
            Method method = obj.getClass().getMethod(methodName, parameterTypes);
            return method.invoke(obj, arguments);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }



    private ReflectUtils() {
    }
}
