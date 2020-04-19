package cn.x5456.summer.stereotype;

import cn.hutool.core.annotation.AnnotationUtil;

import java.lang.annotation.Annotation;

/**
 * @author yujx
 * @date 2020/04/19 10:34
 */
public class AnnotationMetadata {

    private Annotation[] annotations;

    public AnnotationMetadata(Class<?> clazz) {
        this.annotations = AnnotationUtil.getAnnotations(clazz, true);
    }

    public <T> T getAnnotation(Class<T> annotationClass) {
        for (Annotation annotation : annotations) {
            if (annotationClass.isAssignableFrom(annotation.annotationType())) {
                return (T) annotation;
            }
        }
        return null;
    }
}
