package cn.x5456.summer.web.bind.annotation;

public interface ValueConstants {

    /**
     * 定义无默认值的常量-代替{@code null}，我们不能在注释属性中使用它。
     * <p>
     * 这是16个unicode字符的人工排列，其唯一目的是从不与用户声明的值匹配。
     *
     * @see RequestParam#defaultValue()
     */
    String DEFAULT_NONE = "\n\t\t\n\t\t\n\uE000\uE001\uE002\n\t\t\t\t\n";

}
