package cn.x5456.summer;

import cn.x5456.summer.beans.factory.ListableBeanFactory;
import cn.x5456.summer.beans.factory.support.JsonBeanFactoryImpl;

/**
 * @author yujx
 * @date 2020/04/17 20:44
 */
public class TestAB {
    public static void main(String[] args) {
        ListableBeanFactory bf = new JsonBeanFactoryImpl("/Users/x5456/IdeaProjects/Summer/src/test/resources/ab.json");
        System.out.println(bf.getBean("a"));
    }
}
