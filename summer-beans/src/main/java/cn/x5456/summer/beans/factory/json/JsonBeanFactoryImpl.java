package cn.x5456.summer.beans.factory.json;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.x5456.summer.beans.factory.BeanFactory;
import cn.x5456.summer.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import cn.x5456.summer.beans.factory.annotation.Component;
import cn.x5456.summer.beans.factory.annotation.InitAnnotationBeanPostProcessor;
import cn.x5456.summer.beans.factory.annotation.ResourcePostProcessor;
import cn.x5456.summer.beans.factory.config.BeanDefinition;
import cn.x5456.summer.beans.factory.config.BeanPostProcessor;
import cn.x5456.summer.beans.factory.support.DefaultBeanDefinition;
import cn.x5456.summer.beans.factory.support.ListableBeanFactoryImpl;
import cn.x5456.summer.core.util.JsonUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 因为我不想解析xml，所以用json来代替
 * <p>
 * 负责从不同类型文件中读取配置并注册进 bd 的 Map 中
 *
 * @author yujx
 * @date 2020/04/14 15:26
 */
public class JsonBeanFactoryImpl extends ListableBeanFactoryImpl {

    public JsonBeanFactoryImpl(String filePath) {
        this.loadBeanDefinitions(filePath);
    }

    public JsonBeanFactoryImpl(String filePath, BeanFactory parentBeanFactory) {
        super(parentBeanFactory);
        this.loadBeanDefinitions(filePath);
    }

    private void loadBeanDefinitions(String filePath) {
        String json = FileUtil.readUtf8String(filePath);
        Map<String, Object> configMap = JsonUtils.toMap(json, String.class, Object.class);

        List<Map<String, String>> beanDefinitionList = (List<Map<String, String>>) configMap.get("beans");
        if (ObjectUtil.isNotEmpty(beanDefinitionList)) {
            for (Map<String, String> map : beanDefinitionList) {
                BeanDefinition bd = BeanUtil.mapToBeanIgnoreCase(map, DefaultBeanDefinition.class, true);
                super.registerBeanDefinition(bd.getName(), bd);
            }
        }

        // 读取包扫描路径
        List<String> scanPackageNames = (List<String>) configMap.get("componentScanPackages");
        if (ObjectUtil.isNotEmpty(scanPackageNames)) {
            for (String packageName : scanPackageNames) {
                Set<Class<?>> classes = ClassUtil.scanPackage(packageName);
                for (Class<?> clazz : classes) {
                    // 判断是否具有 @Component 注解，并且本身不是注解
                    Component component = AnnotationUtil.getAnnotation(clazz, Component.class);
                    if (ObjectUtil.isNotNull(component) && !clazz.isAnnotation()) {
                        DefaultBeanDefinition bd = new DefaultBeanDefinition();

                        String beanName = StrUtil.isNotBlank(component.value()) ? component.value() : StrUtil.lowerFirst(clazz.getSimpleName());
                        bd.setName(beanName);
                        bd.setClassName(clazz.getName());

                        // TODO: 2020/4/18 参数列表

                        super.registerBeanDefinition(beanName, bd);
                    }
                }
            }
        }

        // 注册几个默认的后置处理器
        DefaultBeanDefinition bdDef = DefaultBeanDefinition.getBD(InitAnnotationBeanPostProcessor.class);
        super.registerBeanDefinition(bdDef.getName(), bdDef);

        DefaultBeanDefinition bdDef2 = DefaultBeanDefinition.getBD(ResourcePostProcessor.class);
        super.registerBeanDefinition(bdDef2.getName(), bdDef2);

        DefaultBeanDefinition bdDef3 = DefaultBeanDefinition.getBD(AutowiredAnnotationBeanPostProcessor.class);
        super.registerBeanDefinition(bdDef3.getName(), bdDef3);

        // 向 beanPostProcessors 中添加后置处理器
        for (BeanPostProcessor beanPostProcessor : super.getBeansOfType(BeanPostProcessor.class).values()) {
            super.addBeanPostProcessor(beanPostProcessor);
        }
    }
}
