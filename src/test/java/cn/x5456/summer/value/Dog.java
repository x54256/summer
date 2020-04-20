package cn.x5456.summer.value;

import cn.x5456.summer.stereotype.ComponentScan;
import cn.x5456.summer.stereotype.Configuration;
import cn.x5456.summer.stereotype.PropertySource;
import cn.x5456.summer.stereotype.Value;

/**
 * @author yujx
 * @date 2020/04/20 14:41
 */
@Configuration
@ComponentScan("cn.x5456.summer.imports")
@PropertySource(value = "/Users/x5456/IdeaProjects/Summer/src/test/resources/value/test.properties")
public class Dog {

    @Value("${dogName}")
    private String name;

    @Override
    public String toString() {
        return "Dog{" +
                "name='" + name + '\'' +
                '}';
    }
}
