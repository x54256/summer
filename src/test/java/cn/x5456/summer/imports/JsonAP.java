package cn.x5456.summer.imports;

import cn.x5456.summer.context.support.FileSystemJsonApplicationContext;

/**
 * @author yujx
 * @date 2020/04/19 11:19
 */
public class JsonAP {
    public static void main(String[] args) {
        FileSystemJsonApplicationContext fileSystemJsonApplicationContext = new FileSystemJsonApplicationContext(new String[]{
                "/Users/x5456/IdeaProjects/Summer/src/test/resources/imports/1.json"
        });

        // testConfiguration
        // System.out.println(fileSystemJsonApplicationContext.getBean("getApple"));

        // TestImportConfiguration
        // System.out.println(fileSystemJsonApplicationContext.getBean("grape"));
        // System.out.println(fileSystemJsonApplicationContext.getBean("apple"));

        // TestImportBeanDefinitionRegistrar
        System.out.println(fileSystemJsonApplicationContext.getBean("apple"));
    }
}
