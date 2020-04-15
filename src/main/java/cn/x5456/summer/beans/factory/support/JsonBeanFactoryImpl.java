package cn.x5456.summer.beans.factory.support;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import cn.x5456.summer.beans.factory.BeanFactory;
import cn.x5456.summer.beans.DefaultBeanDefinition;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 因为我不想解析xml，所以用json来代替
 * <p>
 * 负责从不同类型文件中读取配置并注册进 bd 的 Map 中
 *
 * @author yujx
 * @date 2020/04/14 15:26
 */
public class JsonBeanFactoryImpl extends ListableBeanFactoryImpl {

    public JsonBeanFactoryImpl(String fileName) {
        JSONArray jsonArray = JSONUtil.readJSONArray(new File(fileName), StandardCharsets.UTF_8);
        this.loadBeanDefinitions(jsonArray);
    }

    public JsonBeanFactoryImpl(String fileName, BeanFactory parentBeanFactory) {
        super(parentBeanFactory);

        JSONArray jsonArray = JSONUtil.readJSONArray(new File(fileName), StandardCharsets.UTF_8);
        this.loadBeanDefinitions(jsonArray);
    }

    private void loadBeanDefinitions(JSONArray jsonArray) {
        List<DefaultBeanDefinition> beanDefinitionList = jsonArray.toList(DefaultBeanDefinition.class);
        for (DefaultBeanDefinition bd : beanDefinitionList) {
            super.registerBeanDefinition(bd.getName(), bd);
        }
    }
}
