package cn.x5456.summer.value;

import cn.x5456.summer.context.support.FileSystemJsonApplicationContext;

/**
 * @author yujx
 * @date 2020/04/15 13:35
 */
public class TestJsonAP {

    public static void main(String[] args) {
        FileSystemJsonApplicationContext fileSystemJsonApplicationContext = new FileSystemJsonApplicationContext(new String[]{
                "/Users/x5456/IdeaProjects/Summer/src/test/resources/value/dog.json"
        });

        System.out.println(fileSystemJsonApplicationContext.getBean("dog"));
        System.out.println(fileSystemJsonApplicationContext.getBean("apple"));
    }
}
