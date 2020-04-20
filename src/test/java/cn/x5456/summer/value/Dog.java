package cn.x5456.summer.value;

import cn.x5456.summer.stereotype.Component;
import cn.x5456.summer.stereotype.Value;

/**
 * @author yujx
 * @date 2020/04/20 14:41
 */
@Component
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
