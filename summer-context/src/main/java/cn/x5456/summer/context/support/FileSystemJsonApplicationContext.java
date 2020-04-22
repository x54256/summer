package cn.x5456.summer.context.support;

import cn.x5456.summer.beans.factory.ListableBeanFactory;
import cn.x5456.summer.beans.factory.json.JsonBeanFactoryImpl;
import cn.x5456.summer.beans.factory.support.DefaultBeanDefinition;
import cn.x5456.summer.context.ApplicationContext;
import cn.x5456.summer.context.annotation.ConfigurationClassPostProcessor;
import cn.x5456.summer.core.env.Environment;
import cn.x5456.summer.core.env.StandardEnvironment;

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
        this.loadConfigs(locations);
    }

    private void loadConfigs(String[] locations) {
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

    public FileSystemJsonApplicationContext(String[] locations, ApplicationContext parent) {
        super();
        super.setParent(parent);
        this.loadConfigs(locations);
    }

    /**
     * 初始化当前 ApplicationContext 的 BF
     */
    @Override
    protected void refreshBeanFactory() {
        beanFactory = new JsonBeanFactoryImpl(configLocation, super.getParent());

        // beanFactory 初始化完成后，

        // 注册一个对注解处理的 ConfigurationClassPostProcessor
        DefaultBeanDefinition bdDef = DefaultBeanDefinition.getBD(ConfigurationClassPostProcessor.class);
        beanFactory.registerBeanDefinition(bdDef.getName(), bdDef);

        DefaultBeanDefinition bdDef2 = DefaultBeanDefinition.getBD(PropertySourcesBeanFactoryPostProcessor.class);
        beanFactory.registerBeanDefinition(bdDef2.getName(), bdDef2);

        // 向其中添加一个后置处理器，用于 Aware
        beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
    }

    @Override
    protected Environment createEnvironment() {
        return new StandardEnvironment();
    }

    /**
     * 获取当前 ApplicationContext 的 BF
     */
    @Override
    protected ListableBeanFactory getBeanFactory() {
        return beanFactory;
    }
}
/*
附录：

Spring 5.0 中 ApplicationContext 加载流程

1. new FileSystemXmlApplicationContext() 的时候，FileSystemXmlApplicationContext 会将 locations 设置到成员变量保存
2. 之后 FileSystemXmlApplicationContext 会调用 refresh() 方法，refresh() 方法会初始化 new BF() ，new 出来 bf 的时候就会调用 loadBeanDefinitions(beanFactory);
3. loadBeanDefinitions(beanFactory) 方法就会获取到之前保存的 locations ，循环进行解析将 bd 放进 bdMap 中


Spring 0.9 中 ApplicationContext 加载流程

1. new FileSystemXmlApplicationContext() 的时候，会进行递归的加载，将前一个配置文件加载出来的容器设置为后一个的父容器
2. 然后 FileSystemXmlApplicationContext 会调用 refresh() 方法，refresh() 方法会初始化一个 bf
3. 因为是递归，所以会有多个 bf ， bf 的构造方法中会自动将配置文件中的信息解析进 bdMap 中，而 5.0 中则需要自己调用方法。
 */