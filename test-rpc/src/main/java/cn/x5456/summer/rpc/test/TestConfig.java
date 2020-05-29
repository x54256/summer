package cn.x5456.summer.rpc.test;

import cn.x5456.summer.context.support.FileSystemJsonApplicationContext;

/**
 * @author yujx
 * @date 2020/05/15 10:11
 */
public class TestConfig {

    public static void main(String[] args) {
        FileSystemJsonApplicationContext ap = new FileSystemJsonApplicationContext(
                new String[]{"/Users/x5456/IdeaProjects/Summer/test-rpc/src/main/resources/rpctest.json"}
        );

        // http 测试地址：http://127.0.0.1:20880/?query=%7b%22methodName%22%3a%22func%22%2c%22parameterTypes%22%3a%5b%5d%2c%22arguments%22%3a%5b%5d%2c%22invoker%22%3anull%2c%22attachments%22%3a%7b%22path%22%3a%22cn.x5456.summer.rpc.test.Provider%22%2c%22port%22%3a%2220880%22%7d%7d

//        ap.getBeansOfType(AbstractConfig.class).forEach((k, v) -> {
//            System.out.println("k = " + k + "\tv:" + v);
//        });
//
//        Object serviceAnnotationBeanPostProcessor = ap.getBean("serviceAnnotationBeanPostProcessor");
//        System.out.println(serviceAnnotationBeanPostProcessor);

        ap.getBean("consumer", Consumer.class).test();

    }


}
