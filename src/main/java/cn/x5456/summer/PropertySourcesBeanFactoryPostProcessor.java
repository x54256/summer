package cn.x5456.summer;

import cn.x5456.summer.beans.factory.ListableBeanFactory;
import cn.x5456.summer.env.PropertyResolver;
import cn.x5456.summer.env.PropertySourcesPropertyResolver;

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
public class PropertySourcesBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    // 存放 properties 文件的位置
    // 理论上是 Spring 初始化这个对象的时候，会通过 set 方法依赖注入，但是为了省事，直接在这里写死
    private String[] locations = new String[]{
            "/Users/x5456/IdeaProjects/Summer/src/test/resources/value/test.properties"
    };

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

        // 创建一个 properties 解析器
        PropertyResolver propertyResolver = new PropertySourcesPropertyResolver(propertiesList);

        // 将该解析器放进 beanFactory 中
        beanFactory.addEmbeddedValueResolver(propertyResolver);
    }

    public void setLocations(String... locations) {
        this.locations = locations;
    }
}
