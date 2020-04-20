package cn.x5456.summer;

import cn.x5456.summer.env.Environment;

/**
 * @author yujx
 * @date 2020/04/20 15:04
 */
public interface EnvironmentAware extends Aware {

    void setEnvironment(Environment environment);
}
