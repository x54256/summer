package cn.x5456.summer.beans;

/**
 * @author yujx
 * @date 2020/04/16 17:32
 */
public class PropertyValue {

    /**
     * Property name
     */
    private String name;

    /**
     * Value of the property
     */
    private Object value;

    public PropertyValue(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
