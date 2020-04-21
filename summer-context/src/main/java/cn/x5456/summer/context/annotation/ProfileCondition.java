package cn.x5456.summer.context.annotation;

import cn.hutool.core.collection.CollUtil;
import cn.x5456.summer.core.type.AnnotationMetadata;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author yujx
 * @date 2020/04/20 16:21
 */
public class ProfileCondition implements Condition {

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotationMetadata metadata) {
        String[] activeProfiles = conditionContext.getEnvironment().getActiveProfiles();
        String[] profileValue = metadata.getAnnotation(Profile.class).value();

        Collection<String> intersection = CollUtil.intersection(Arrays.asList(activeProfiles), Arrays.asList(profileValue));
        return CollUtil.isNotEmpty(intersection);
    }
}
