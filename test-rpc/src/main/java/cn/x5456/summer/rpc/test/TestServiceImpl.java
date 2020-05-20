package cn.x5456.summer.rpc.test;

import cn.x5456.summer.rpc.config.summer.Service;

/**
 * @author yujx
 * @date 2020/05/20 09:24
 */
@Service
public class TestServiceImpl implements TestService {

    public void func() {
        System.out.println(123);
    }
}
