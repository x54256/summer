package cn.x5456.summer;

/**
 * @author yujx
 * @date 2020/04/15 13:34
 */
public class Grape {

    private String name;

    private Apple apple;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Apple getApple() {
        return apple;
    }

    public void setApple(Apple apple) {
        this.apple = apple;
    }

    @Override
    public String toString() {
        return "Grape{" +
                "name='" + name + '\'' +
                ", apple=" + apple +
                '}';
    }
}
