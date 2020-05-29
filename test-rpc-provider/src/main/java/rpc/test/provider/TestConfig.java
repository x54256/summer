package rpc.test.provider;

import cn.x5456.summer.context.support.FileSystemJsonApplicationContext;

/**
 * @author yujx
 * @date 2020/05/15 10:11
 */
public class TestConfig {

    public static void main(String[] args) {
        FileSystemJsonApplicationContext ap = new FileSystemJsonApplicationContext(
                new String[]{"/Users/x5456/IdeaProjects/Summer/test-rpc/test-rpc-provider/src/main/resources/provider.json"}
        );


    }


}
