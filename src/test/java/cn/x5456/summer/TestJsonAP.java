package cn.x5456.summer;

import cn.x5456.summer.context.support.FileSystemJsonApplicationContext;

/**
 * @author yujx
 * @date 2020/04/15 13:35
 */
public class TestJsonAP {

    public static void main(String[] args) {
        FileSystemJsonApplicationContext fileSystemJsonApplicationContext = new FileSystemJsonApplicationContext(new String[]{
                "/Users/x5456/IdeaProjects/Summer/src/test/resources/apple.json",
                "/Users/x5456/IdeaProjects/Summer/src/test/resources/grape.json"
        });

        System.out.println(fileSystemJsonApplicationContext.getBean("apple"));
        System.out.println(fileSystemJsonApplicationContext.getBean("grape"));
        System.out.println(fileSystemJsonApplicationContext.getBeansOfType(Grape.class));
        System.out.println(fileSystemJsonApplicationContext.getBeansOfType(Apple.class));
    }
}
