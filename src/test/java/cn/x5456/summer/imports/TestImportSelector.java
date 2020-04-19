package cn.x5456.summer.imports;

import cn.hutool.core.util.ObjectUtil;
import cn.x5456.summer.Apple;
import cn.x5456.summer.Grape;
import cn.x5456.summer.stereotype.AnnotationMetadata;
import cn.x5456.summer.stereotype.Configuration;
import cn.x5456.summer.stereotype.ImportSelector;

/**
 * @author yujx
 * @date 2020/04/19 11:24
 */
public class TestImportSelector implements ImportSelector {
    /**
     * @param importingClassMetadata 被 @Import 注解的类的注解元数据
     */
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        Configuration annotation = importingClassMetadata.getAnnotation(Configuration.class);
        if (ObjectUtil.isEmpty(annotation.value())) {
            return new String[]{Apple.class.getName()};
        } else {
            return new String[]{Grape.class.getName()};
        }
    }
}
