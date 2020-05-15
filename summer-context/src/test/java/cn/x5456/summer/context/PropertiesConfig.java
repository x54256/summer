package cn.x5456.summer.context;

import cn.x5456.summer.context.annotation.Configuration;
import cn.x5456.summer.context.annotation.PropertySource;
import cn.x5456.summer.context.properties.EnableConfigurationProperties;

/**
 * @author yujx
 * @date 2020/05/15 16:10
 */
@Configuration
@EnableConfigurationProperties
@PropertySource("/Users/x5456/IdeaProjects/Summer/summer-context/src/test/java/cn/x5456/summer/context/a.properties")
public class PropertiesConfig {
}
