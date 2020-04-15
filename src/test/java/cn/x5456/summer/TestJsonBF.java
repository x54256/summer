package cn.x5456.summer;

import cn.x5456.summer.beans.factory.ListableBeanFactory;
import cn.x5456.summer.beans.factory.support.JsonBeanFactoryImpl;

import java.util.Map;

/**
 * @author yujx
 * @date 2020/04/14 15:41
 */
public class TestJsonBF {
    public static void main(String[] args) {
        ListableBeanFactory bf = new JsonBeanFactoryImpl("/Users/x5456/IdeaProjects/Summer/src/test/resources/apple.json");
        Apple apple = bf.getBean("apple", Apple.class);
        System.out.println(apple);

        Map<String, Apple> beansOfType = bf.getBeansOfType(Apple.class);
        System.out.println(beansOfType);
    }
}
