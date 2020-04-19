package cn.x5456.summer.imports;

import cn.x5456.summer.Apple;
import cn.x5456.summer.stereotype.Bean;

/**
 * @author yujx
 * @date 2020/04/19 11:21
 */
public class TestImportConfiguration {

    @Bean
    public Apple getApple() {
        return new Apple();
    }
}
