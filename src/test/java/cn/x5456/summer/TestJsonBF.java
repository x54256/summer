package cn.x5456.summer;

import cn.x5456.summer.beans.factory.ListableBeanFactory;
import cn.x5456.summer.beans.factory.support.JsonBeanFactoryImpl;

/**
 * @author yujx
 * @date 2020/04/14 15:41
 */
public class TestJsonBF {
    public static void main(String[] args) {
        ListableBeanFactory bf = new JsonBeanFactoryImpl("/Users/x5456/IdeaProjects/Summer/src/test/resources/apple.json");
        Grape grape = bf.getBean("grape", Grape.class);
        System.out.println(grape);
    }
}
