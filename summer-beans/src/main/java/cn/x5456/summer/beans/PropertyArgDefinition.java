package cn.x5456.summer.beans;

/**
 * @author yujx
 * @date 2020/04/16 17:33
 */
public class PropertyArgDefinition {

    /**
     * 属性名称
     */
    private String name;

    /**
     * 类型
     */
    private String type;

    /**
     * 参数值
     */
    private String value;

    /**
     * 引用名称
     */
    private String refName;

    public PropertyArgDefinition() {
    }

    public PropertyArgDefinition(String name, String type, String value, String refName) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.refName = refName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRefName() {
        return refName;
    }

    public void setRefName(String refName) {
        this.refName = refName;
    }
}
