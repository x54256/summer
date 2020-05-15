package cn.x5456.summer.context;

import cn.x5456.summer.beans.factory.annotation.Component;
import cn.x5456.summer.context.properties.ConfigurationProperties;

/**
 * @author yujx
 * @date 2020/05/15 16:10
 */
@Component
@ConfigurationProperties("apple")
public class Apple {

    private String name;

    private Integer age;

    @Override
    public String toString() {
        return "Apple{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
