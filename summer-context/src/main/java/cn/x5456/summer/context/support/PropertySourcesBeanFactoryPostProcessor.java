package cn.x5456.summer.context.support;

import cn.x5456.summer.beans.factory.ListableBeanFactory;
import cn.x5456.summer.beans.factory.config.BeanFactoryPostProcessor;
import cn.x5456.summer.context.EnvironmentAware;
import cn.x5456.summer.core.env.Environment;
import cn.x5456.summer.core.env.PropertyResolver;
import cn.x5456.summer.core.env.PropertySourcesPropertyResolver;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author yujx
 * @date 2020/04/20 13:56
 */
public class PropertySourcesBeanFactoryPostProcessor implements BeanFactoryPostProcessor, EnvironmentAware {

    // 存放 properties 文件的位置
    // 理论上是 Spring 初始化这个对象的时候，会通过 set 方法依赖注入，但是为了省事，直接在这里写死
    private String[] locations = new String[]{
//            "/Users/x5456/IdeaProjects/Summer/src/test/resources/value/test.properties"
    };

    private Environment environment;

    /**
     * 向 bf 中添加一个 @Value 解析器
     */
    @Override
    public void postProcessBeanFactory(ListableBeanFactory beanFactory) {

        // 处理 locations 成 List<Properties>
        List<Properties> propertiesList = new ArrayList<>();
        for (String location : locations) {

            // 将文件读入 Properties 中
            try {
                FileInputStream inputStream = new FileInputStream(location);
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                Properties properties = new Properties();
                properties.load(inputStreamReader);
                propertiesList.add(properties);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        // 如果 applicationContext 中的 env 不为空，则把其中加载的 Properties 也加入
        // 通过这段代码可以得出，用 xml 配置的 properties 文件，从 env 中读不出来，但是通过注解 @PropertySource 的两者都能获取到
        if (this.environment != null) {
            List<Properties> envPropertySources = environment.getPropertySources();
            propertiesList.addAll(envPropertySources);
        }

        // 创建一个 properties 解析器
        PropertyResolver propertyResolver = new PropertySourcesPropertyResolver(propertiesList);

        // 将该解析器放进 beanFactory 中
        beanFactory.addEmbeddedValueResolver(propertyResolver);
    }

    public void setLocations(String... locations) {
        this.locations = locations;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
