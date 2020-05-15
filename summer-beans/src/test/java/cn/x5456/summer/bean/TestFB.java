package cn.x5456.summer.bean;

import cn.x5456.summer.beans.factory.json.JsonBeanFactoryImpl;

import java.util.Arrays;

/**
 * @author yujx
 * @date 2020/05/15 13:11
 */
public class TestFB {

    public static void main(String[] args) {
        JsonBeanFactoryImpl bf = new JsonBeanFactoryImpl(
                "/Users/x5456/IdeaProjects/Summer/summer-beans/src/test/java/fbTest.json"
        );

        System.out.println(bf.getBean("&apple"));
        System.out.println(bf.getBean("apple"));
        System.out.println(Arrays.toString(bf.getBeanDefinitionNames(Apple.class)));
    }
}
