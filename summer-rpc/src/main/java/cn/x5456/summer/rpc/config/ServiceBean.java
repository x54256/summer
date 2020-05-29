package cn.x5456.summer.rpc.config;

import cn.x5456.summer.beans.factory.BeanNameAware;
import cn.x5456.summer.beans.factory.InitializingBean;
import cn.x5456.summer.context.ApplicationContext;
import cn.x5456.summer.context.ApplicationContextAware;
import cn.x5456.summer.context.ApplicationListener;
import cn.x5456.summer.context.event.ContextRefreshedEvent;
import cn.x5456.summer.core.util.ReflectUtils;
import cn.x5456.summer.rpc.Exporter;
import cn.x5456.summer.rpc.Invoker;
import cn.x5456.summer.rpc.protocol.Protocol;
import cn.x5456.summer.rpc.proxy.ProtocolFilterWrapper;
import cn.x5456.summer.rpc.proxy.ProxyFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

public class ServiceBean<T> implements InitializingBean, ApplicationContextAware, ApplicationListener<ContextRefreshedEvent>, BeanNameAware {

    private String beanName;

    private ApplicationContext applicationContext;

    // 接口的字节码类型
    private String interfaceClass;

    // 引用的接口的实现
    private T ref;

    // 服务提供者通用配置
    private ProviderConfig providerConfig;

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

    /**
     * 从容器中取出配置信息（Dubbo 中是用 List 装的，我们为了方便）
     */
    @Override
    public void afterPropertiesSet() {
        this.providerConfig = applicationContext.getBean("providerConfig", ProviderConfig.class);
        this.applicationConfig = applicationContext.getBean("applicationConfig", ApplicationConfig.class);
        this.registryConfig = applicationContext.getBean("registryConfig", RegistryConfig.class);
        this.protocolConfig = applicationContext.getBean("protocolConfig", ProtocolConfig.class);

        this.proxyFactoryMap = applicationContext.getBeansOfType(ProxyFactory.class);
        this.protocolMap = applicationContext.getBeansOfType(Protocol.class);
    }

    /**
     * 容器刷新完毕时触发，发布服务
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent e) {
        this.export();
    }

    private void export() {
        this.doExport();
    }

    private void doExport() {
        this.doExportUrls();
    }

    private void doExportUrls() {
        // TODO: 2020/5/19 根据 RegistryConfig 拼接注册中心的地址

        // 根据协议发布
        this.doExportUrlsFor1Protocol(protocolConfig);
    }

    /*
    injvm://192.168.2.94:65371/com.alibaba.dubbo.demo.DemoService?
    application=demo-provider&
    bean.name=com.alibaba.dubbo.demo.DemoService&
    bind.ip=192.168.2.94&
    bind.port=65371&
    dubbo=2.0.2&
    generic=false&
    interface=com.alibaba.dubbo.demo.DemoService&
    methods=sayHello&
    notify=false&
    pid=33365&
    qos.port=22222
    &side=provider&
    timestamp=1589507274731
     */
    private void doExportUrlsFor1Protocol(ProtocolConfig protocolConfig) {

        // 根据协议拼接 url
        String url = protocolConfig.getName() + "://" + this.getLocalHost() + ":" + protocolConfig.getPort()
                + "/" + interfaceClass + "?application=" + applicationConfig.getName()
                + "&bean.name=" + beanName + "&bind.ip=" + this.getLocalHost() + "&bind.port=" + protocolConfig.getPort()
                + "&interface=" + interfaceClass;

        // 根据 url 获取不同的 Invoker 工厂（当然我这里的条件很简单，就是根据协议名获取），创建 Invoker
        ProxyFactory proxyFactory = proxyFactoryMap.get("javassistProxyFactory");
        Invoker<T> invoker = proxyFactory.getInvoker(ref, (Class<T>) ReflectUtils.getType(interfaceClass), url);

        // 为 invoker 套上对应协议的过滤器，然后发布
        String protocolName = url.substring(0, url.indexOf("://"));
        Protocol protocol = protocolMap.get(protocolName + "Protocol");
        ProtocolFilterWrapper protocolFilterWrapper = new ProtocolFilterWrapper(protocol);
        Exporter<T> export = protocolFilterWrapper.export(invoker);
    }

    private String getLocalHost() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Class<ContextRefreshedEvent> getEventType() {
        return ContextRefreshedEvent.class;
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) {
        this.applicationContext = ctx;
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }
}