package cn.x5456.summer.beans;

/**
 * 默认实现（Spring 中有两个实现 RootBD 和 ChildBD 其中 ChildBD 我们基本用不到）
 *
 * @author yujx
 * @date 2020/04/14 15:05
 */
public class DefaultBeanDefinition implements BeanDefinition {

    // 名称
    private String name;

    // 全类名
    private String className;

    // 生命周期
    private ScopeEnum scope = ScopeEnum.SINGLETON;

    /**
     * 名称
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * 设置名称
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 类名称
     */
    @Override
    public String getClassName() {
        return className;
    }

    /**
     * 设置类名称
     */
    @Override
    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public ScopeEnum getScope() {
        return scope;
    }

    @Override
    public void setScope(ScopeEnum scope) {
        this.scope = scope;
    }
}
