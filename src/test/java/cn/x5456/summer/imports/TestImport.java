package cn.x5456.summer.imports;

import cn.x5456.summer.stereotype.Configuration;
import cn.x5456.summer.stereotype.Import;

/**
 * @author yujx
 * @date 2020/04/19 11:21
 */
@Configuration
// @Import(value = TestImportConfiguration.class)
// @Import(value = TestImportSelector.class)
@Import(value = TestImportBeanDefinitionRegistrar.class)
public class TestImport {
}
