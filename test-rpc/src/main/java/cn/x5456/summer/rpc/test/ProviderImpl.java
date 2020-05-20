package cn.x5456.summer.rpc.test;

import cn.x5456.summer.rpc.config.summer.Service;

/**
 * @author yujx
 * @date 2020/05/20 14:59
 */
@Service
public class ProviderImpl implements Provider {
    @Override
    public Object func() {
        return "啦啦啦";
    }

    @Override
    public Integer haveArgs(Integer a, Integer b) {
        return a + b;
    }
}
