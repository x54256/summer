package cn.x5456.summer;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.x5456.summer.beans.BeanDefinition;
import cn.x5456.summer.beans.DefaultBeanDefinition;
import cn.x5456.summer.beans.factory.BeanDefinitionRegistry;
import cn.x5456.summer.beans.factory.ListableBeanFactory;
import cn.x5456.summer.stereotype.*;
import cn.x5456.summer.util.ReflectUtils;

import java.lang.reflect.Method;

/**
 * @author yujx
 * @date 2020/04/18 17:37
 */
public class ConfigurationClassPostProcessor implements BeanDefinitionRegistryPostProcessor {

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
        ListableBeanFactory bf = (ListableBeanFactory) registry;

        for (String bdName : bf.getBeanDefinitionNames()) {
            BeanDefinition classBeanDefinition = registry.getBeanDefinition(bdName);
            String className = classBeanDefinition.getClassName();
            Class<?> clazz = ReflectUtils.getType(className);

            // 找到类上携带 @Configuration 的
            Configuration configuration = AnnotationUtil.getAnnotation(clazz, Configuration.class);
            if (ObjectUtil.isNotEmpty(configuration)) {
                // 处理 @Configuration 标注的类
                this.processConfigurationClass(registry, classBeanDefinition, clazz);
            }
        }
    }

    private void processConfigurationClass(BeanDefinitionRegistry registry, BeanDefinition classBeanDefinition, Class<?> clazz) {
        // 1、处理 @Import 注解
        Import annotation = AnnotationUtil.getAnnotation(clazz, Import.class);
        if (ObjectUtil.isNotEmpty(annotation) && ObjectUtil.isNotEmpty(annotation.value())) {
            Class<?>[] importClasses = annotation.value();

            // 获取被标注 @Import 注解类上的所有注解信息
            AnnotationMetadata annotationMetadata = new AnnotationMetadata(clazz);

            this.processImport(registry, importClasses, annotationMetadata);
        }

        // 2、处理 @Bean 注解
        this.processBean(registry, classBeanDefinition, clazz);
    }

    private void processImport(BeanDefinitionRegistry registry, Class<?>[] importClasses, AnnotationMetadata annotationMetadata) {

        for (Class<?> importClass : importClasses) {

            // 1、将其 @Import 的类加入 bdMap 中
            DefaultBeanDefinition bdDef = new DefaultBeanDefinition();
            String beanName = StrUtil.lowerFirst(importClass.getSimpleName());
            bdDef.setName(beanName);
            bdDef.setClassName(importClass.getName());

            registry.registerBeanDefinition(beanName, bdDef);

            // 2、处理 Import 类型为 ImportSelector 的
            if (ImportSelector.class.isAssignableFrom(importClass)) {
                ImportSelector importSelector = (ImportSelector) ReflectUtil.newInstance(importClass);

                // 根据注解信息选择 Import 哪个类
                String[] selectors = importSelector.selectImports(annotationMetadata);
                Class<?>[] selectorClasses = new Class<?>[selectors.length];
                for (int i = 0; i < selectors.length; i++) {
                    selectorClasses[i] = ReflectUtils.getType(selectors[i]);
                }

                // 把 importSelector 选择的类当作 @Import 的，进行递归处理
                this.processImport(registry, selectorClasses, annotationMetadata);
            }

            // 3、处理 Import 类型为 ImportBeanDefinitionRegistrar 的
            else if (ImportBeanDefinitionRegistrar.class.isAssignableFrom(importClass)) {
                ImportBeanDefinitionRegistrar importBeanDefinitionRegistrar = (ImportBeanDefinitionRegistrar) ReflectUtil.newInstance(importClass);
                importBeanDefinitionRegistrar.registerBeanDefinitions(annotationMetadata, registry);
            }

            // 4、处理其他类型的，把它当作 @Configuration 注解标注的进行处理
            else {
                this.processConfigurationClass(registry, bdDef, importClass);
            }
        }
    }

    private void processBean(BeanDefinitionRegistry registry, BeanDefinition classBeanDefinition, Class<?> clazz) {
        // 循环找方法中包含 @Bean 注解的
        for (Method method : clazz.getMethods()) {
            if (method.isAnnotationPresent(Bean.class)) {
                BeanDefinition bdDef = new DefaultBeanDefinition();

                Bean bean = method.getAnnotation(Bean.class);
                String beanName = ObjectUtil.isNotEmpty(bean.value()) ? bean.value() : method.getName();

                bdDef.setName(beanName);
                bdDef.setFactoryBean(classBeanDefinition.getName());
                bdDef.setFactoryMethod(method.getName());
                bdDef.setInitMethod(bean.initMethod());
                bdDef.setDestroyMethod(bean.destroyMethod());

                // TODO: 2020/4/18 属性列表

                registry.registerBeanDefinition(beanName, bdDef);
            }
        }
    }
}