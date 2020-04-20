package cn.x5456.summer.env;

import java.util.List;
import java.util.Properties;

/**
 * 子类负责注入配置
 *
 * @author yujx
 * @date 2020/04/20 13:36
 */
public class StandardEnvironment extends AbstractEnvironment {

    public StandardEnvironment() {
        super();
    }

    @Override
    protected void customizePropertySources(List<Properties> propertySources) {
        // StandardEnvironment 中 Spring 给他添加了这些东西 systemEnvironment、systemProperties

        // StandardServletEnvironment 中 Spring 给他添加了这些东西 servletContextInitParams、servletConfigInitParams
        // 所以没有使用 SpringBoot 的时候 spring.profiles.active 需要配置在 web.xml 中
        /*
        <context-param>
            <param-name>spring.profiles.active</param-name>
            <param-value>dev</param-value>
        </context-param>
         */

        // application.xml 中引入的 properties 只用于 @Value 的那个后置处理器使用，如果需要在 env 中也使用，需要配合 @PropertiesSources 注解
        // <context:property-placeholder location="classpath:db.properties" />

        // SpringBoot 中 application.yml 在哪添加的我忘了，反正是获取到了这个 list ，然后直接向里面 add 的。
    }
}
