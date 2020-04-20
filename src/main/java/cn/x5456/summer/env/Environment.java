package cn.x5456.summer.env;

/**
 * Environment 继承了 PropertyResolver，它可以解析我们给他提供的配置
 *
 * @author yujx
 * @date 2020/04/20 09:34
 */
public interface Environment extends PropertyResolver {

    String[] getActiveProfiles();

    String[] getDefaultProfiles();
}
