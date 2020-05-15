package cn.x5456.summer.context;

import cn.x5456.summer.context.support.FileSystemJsonApplicationContext;

/**
 * @author yujx
 * @date 2020/05/15 16:09
 */
public class PropertiesTest {

    public static void main(String[] args) {
        FileSystemJsonApplicationContext context = new FileSystemJsonApplicationContext(new String[]{
                "/Users/x5456/IdeaProjects/Summer/summer-context/src/test/java/cn/x5456/summer/context/propertiesTest.json"
        });

        System.out.println(context.getBean("apple"));
    }
}
