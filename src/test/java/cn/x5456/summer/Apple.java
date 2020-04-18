package cn.x5456.summer;

import cn.x5456.summer.stereotype.Component;

/**
 * @author yujx
 * @date 2020/04/14 15:40
 */
@Component
public class Apple {

    private String name = "红富士";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Apple{" +
                "name='" + name + '\'' +
                '}';
    }

}
