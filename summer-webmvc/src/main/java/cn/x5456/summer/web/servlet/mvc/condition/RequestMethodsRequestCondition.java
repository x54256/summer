package cn.x5456.summer.web.servlet.mvc.condition;

import cn.x5456.summer.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author yujx
 * @date 2020/04/24 14:21
 */
public class RequestMethodsRequestCondition implements RequestCondition<RequestMethodsRequestCondition> {

    private final Set<RequestMethod> methods = new HashSet<>();

    public RequestMethodsRequestCondition(RequestMethod... methods) {
        this(Arrays.asList(methods));
    }

    private RequestMethodsRequestCondition(Collection<RequestMethod> requestMethods) {
        this.methods.addAll(requestMethods);
    }

    /**
     * 将两个 RequestCondition 对象结合
     */
    @Override
    public RequestMethodsRequestCondition combine(RequestMethodsRequestCondition other) {
        Set<RequestMethod> result = new HashSet<>();
        result.addAll(this.methods);
        result.addAll(other.methods);

        return new RequestMethodsRequestCondition(result);
    }

    /**
     * 检查此条件是否与给定请求匹配，并返回潜在的新请求条件，其中包含适合当前请求的内容。
     * <p>
     * 不匹配返回 null
     */
    @Override
    public RequestMethodsRequestCondition getMatchingCondition(HttpServletRequest request) {
        if (methods.isEmpty()) {
            return new RequestMethodsRequestCondition();
        }

        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        if (methods.contains(requestMethod)) {
            return new RequestMethodsRequestCondition(requestMethod);
        }

        return null;
    }
}
