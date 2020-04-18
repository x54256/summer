package cn.x5456.summer.context.support;

import cn.x5456.summer.ApplicationContextAwareProcessor;
import cn.x5456.summer.ConfigurationClassPostProcessor;
import cn.x5456.summer.beans.DefaultBeanDefinition;
import cn.x5456.summer.beans.factory.ListableBeanFactory;
import cn.x5456.summer.beans.factory.support.JsonBeanFactoryImpl;

/**
 * 读取 json 文件的 ApplicationContext
 * <p>
 * 它负责调用 AbstractApplicationContext 定义的 refresh() 方法，然后父类会调用它的 refreshBeanFactory() 方法，初始化 bean 工厂
 * <p>
 * 如果传入的是文件数组，那么前一个是后一个的父容器
 *
 * @author yujx
 * @date 2020/04/15 12:39
 */
public class FileSystemJsonApplicationContext extends AbstractApplicationContext {

    // 当前所在配置文件的路径
    private String configLocation;

    // 与当前 ApplicationContext 相关联的 BF
    private ListableBeanFactory beanFactory;


    /*
    我给大家捋一捋，假设用户会传入 2 个文件路径：new String[]{applicationContext.xml, applicationContext2.xml}

    1. 首先会将 configLocation 设置为 applicationContext.xml
    2. 判断长度是否大于 1 返回 true，进入 if 代码块，将 temp 赋值为 new String[]{applicationContext2.xml}，然后"递归"的 new 了一个 FileSystemJsonApplicationContext
        2.1 递归里，将 configLocation 设置为 applicationContext2.xml
        2.2 判断，返回 false 则执行到了 refresh() 方法，refresh() 方法中调用了 refreshBeanFactory() 方法
        2.3 refreshBeanFactory() 方法中先获取了当前 ApplicationContext 的父容器，因为这个是第一个被初始化的，所以为 null；
        之后将当前的 ApplicationContext （我们称为 AP1）的 BF 设置为新 new 出来的（称为 BF1） 【AP1 中包含 BF1】
        2.4 然后"递归"返回，返回了 AP1
    3. 将 AP1 设置为当前 AP2 的父容器【AP1 为 AP2 的父容器】
    4. 调用 refresh() 方法，refresh() 方法中调用了 refreshBeanFactory() 方法
    5. refreshBeanFactory() 方法中获取到了父容器 AP1，将 AP1 设置为当前 new 的 BF2 的父BF【BF2 的父BF 为 AP1】
    6. 然后将 BF2 设置为 AP2 中的 BF 属性上 【AP2 中包含 BF2】
    7. 结束，返回AP2

    ![](https://tva1.sinaimg.cn/large/007S8ZIlly1gdue28iu6qj30x40jg0u0.jpg)

    为什么BF2的父BF是AP1而不是BF1？

    我一开始想的是如果BF2中没有就回去BF1那里找，找到之后就直接返回了，没有对Aware进行配置，，但看了下代码发现这个说法是错的，AP2获取到了bean之后也会对Aware进行配置，不管是不是父容器。可能原因是他们不想让ApplicationContext这个接口加一个getParentBF的方法，反正Application也只是对BF进行的包装，用谁都一样
     */
    public FileSystemJsonApplicationContext(String[] locations) {
        // 将用户传入的数组的最后一个文件路径作为当前需要初始化的文件路径
        configLocation = locations[locations.length - 1];

        if (locations.length > 1) {
            String[] temp = new String[locations.length - 1];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = locations[i];
            }

            // 这个地方返回的是当前索引的上一个索引 new 出来的对象，所以要将这个 applicationContext 设为当前路径索引的父容器
            // 假设用户传入2个文件路径，这个会返回用第二个文件创造出来的applicationContext，要将其设置为当前（也就是第一个文件创造出来的）容器的父容器
            FileSystemJsonApplicationContext applicationContext = new FileSystemJsonApplicationContext(temp);
            // 设置父容器的目的是为了下面 refresh() 时能够找到它，并将其作为父 BF。
            super.setParent(applicationContext);
        }

        // 刷新容器，会调用 refreshBeanFactory() 方法
        super.refresh();
    }

    /**
     * 初始化当前 ApplicationContext 的 BF
     */
    @Override
    protected void refreshBeanFactory() {
        beanFactory = new JsonBeanFactoryImpl(configLocation, super.getParent());

        // beanFactory 初始化完成后，

        // 注册一个对注解处理的 ConfigurationClassPostProcessor
        DefaultBeanDefinition bd = new DefaultBeanDefinition();
        bd.setName("configurationClassPostProcessor");
        bd.setClassName(ConfigurationClassPostProcessor.class.getName());
        beanFactory.registerBeanDefinition(bd.getName(), bd);

        // 向其中添加一个后置处理器
        beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
    }

    /**
     * 获取当前 ApplicationContext 的 BF
     */
    @Override
    protected ListableBeanFactory getBeanFactory() {
        return beanFactory;
    }
}
