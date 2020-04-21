package cn.x5456.summer.context;

import cn.x5456.summer.beans.factory.Aware;
import cn.x5456.summer.core.env.Environment;

/**
 * @author yujx
 * @date 2020/04/20 15:04
 */
public interface EnvironmentAware extends Aware {

    void setEnvironment(Environment environment);
}
