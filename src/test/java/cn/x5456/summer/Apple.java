package cn.x5456.summer;

import cn.x5456.summer.stereotype.Bean;
import cn.x5456.summer.stereotype.Configuration;

/**
 * @author yujx
 * @date 2020/04/14 15:40
 */
@Configuration
public class Apple {

    @Bean
    public Grape func() {
        return new Grape();
    }

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
