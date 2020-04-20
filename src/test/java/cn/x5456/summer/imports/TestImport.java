package cn.x5456.summer.imports;

import cn.x5456.summer.stereotype.*;

/**
 * @author yujx
 * @date 2020/04/19 11:21
 */

@Profile("default")
//@Conditional(value = TestCondition.class)
@Configuration
// @Import(value = TestImportConfiguration.class)
// @Import(value = TestImportSelector.class)
@Import(value = TestImportBeanDefinitionRegistrar.class)
public class TestImport {
}

//class TestCondition implements Condition {
//
//
//    /**
//     * 结果是否匹配
//     *
//     * @param conditionContext
//     * @param metadata
//     */
//    @Override
//    public boolean matches(ConditionContext conditionContext, AnnotationMetadata metadata) {
//        return false;
//    }
//}