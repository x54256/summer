package cn.x5456.summer.rpc.test;

import cn.x5456.summer.context.support.FileSystemJsonApplicationContext;
import cn.x5456.summer.rpc.config.AbstractConfig;

/**
 * @author yujx
 * @date 2020/05/15 10:11
 */
public class TestConfig {

    public static void main(String[] args) {
        FileSystemJsonApplicationContext ap = new FileSystemJsonApplicationContext(
                new String[]{"/Users/x5456/IdeaProjects/Summer/test-rpc/src/main/resources/rpctest.json"}
        );

        ap.getBeansOfType(AbstractConfig.class).forEach((k, v) -> {
            System.out.println("k = " + k + "\tv:" + v);
        });

        Object serviceAnnotationBeanPostProcessor = ap.getBean("serviceAnnotationBeanPostProcessor");
        System.out.println(serviceAnnotationBeanPostProcessor);

    }


}
