package cn.x5456.summer.rpc.config;

import cn.x5456.summer.beans.factory.FactoryBean;
import cn.x5456.summer.beans.factory.InitializingBean;
import cn.x5456.summer.context.ApplicationContext;
import cn.x5456.summer.context.ApplicationContextAware;
import cn.x5456.summer.core.util.ReflectUtils;
import cn.x5456.summer.rpc.Invoker;
import cn.x5456.summer.rpc.protocol.Protocol;
import cn.x5456.summer.rpc.proxy.ProtocolFilterWrapper;
import cn.x5456.summer.rpc.proxy.ProxyFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

/**
 * @author yujx
 * @date 2020/05/20 11:04
 */
public class ReferenceBean<T> implements FactoryBean<T>, ApplicationContextAware, InitializingBean {

    // 接口的字节码类型
    private String interfaceClass;

    private ApplicationContext applicationContext;

    // 服务消费者全局配置
    private ConsumerConfig consumerConfig;

    // 应用配置
    private ApplicationConfig applicationConfig;

    // 注册中心配置
    private RegistryConfig registryConfig;

    // 协议配置
    private ProtocolConfig protocolConfig;

    // 代理工厂 map
    private Map<String, ProxyFactory> proxyFactoryMap;

    // 协议 map
    private Map<String, Protocol> protocolMap;

    @Override
    public void setApplicationContext(ApplicationContext ctx) {
        this.applicationContext = ctx;
    }

    @Override
    public void afterPropertiesSet() {
        this.consumerConfig = applicationContext.getBean("consumerConfig", ConsumerConfig.class);
        this.applicationConfig = applicationContext.getBean("applicationConfig", ApplicationConfig.class);
        this.registryConfig = applicationContext.getBean("registryConfig", RegistryConfig.class);
        this.protocolConfig = applicationContext.getBean("protocolConfig", ProtocolConfig.class);

        this.proxyFactoryMap = applicationContext.getBeansOfType(ProxyFactory.class);
        this.protocolMap = applicationContext.getBeansOfType(Protocol.class);
    }

    /**
     * 生成代理对象
     */
    @Override
    public T getObject() {
        return init();
    }

    private T init() {
        // 根据协议拼接 url
        String url = protocolConfig.getName() + "://" + this.getLocalHost() + ":" + protocolConfig.getPort()
                + "/" + interfaceClass + "?application=" + applicationConfig.getName()
                + "&interface=" + interfaceClass;

        // 根据 url 获取不同的 Invoker 工厂（当然我这里的条件很简单，就是根据协议名获取），创建 Invoker
        String protocolName = url.substring(0, url.indexOf("://"));
        Protocol protocol = protocolMap.get(protocolName + "Protocol");

        // 根据协议获取执行器
        Invoker<?> invoker = new ProtocolFilterWrapper(protocol).refer(ReflectUtils.getType(interfaceClass), url);
        ProxyFactory proxyFactory = proxyFactoryMap.get("javassistProxyFactory");
        return (T) proxyFactory.getProxy(invoker);
    }

    private String getLocalHost() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Class<?> getObjectType() {
        return ReflectUtils.getType(interfaceClass);
    }
}
