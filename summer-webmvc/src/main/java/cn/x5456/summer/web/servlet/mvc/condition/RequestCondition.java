package cn.x5456.summer.web.servlet.mvc.condition;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求条件对象
 */
public interface RequestCondition<T> {

    /**
     * 将两个 RequestCondition 对象结合
     */
    T combine(T other);

    /**
     * 检查此条件是否与给定请求匹配，并返回潜在的新请求条件，其中包含适合当前请求的内容。
     * <p>
     * 不匹配返回 null
     */
    T getMatchingCondition(HttpServletRequest request);

}