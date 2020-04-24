package cn.x5456.summer.web.servlet.mvc.method;

import cn.hutool.core.util.ObjectUtil;
import cn.x5456.summer.web.bind.annotation.RequestMethod;
import cn.x5456.summer.web.servlet.mvc.condition.PatternsRequestCondition;
import cn.x5456.summer.web.servlet.mvc.condition.RequestCondition;
import cn.x5456.summer.web.servlet.mvc.condition.RequestMethodsRequestCondition;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yujx
 * @date 2020/04/24 10:02
 */
public class RequestMappingInfo implements RequestCondition<RequestMappingInfo> {

    private PatternsRequestCondition patternsCondition;

    private RequestMethodsRequestCondition requestMethodsRequestCondition;

    private RequestMappingInfo(PatternsRequestCondition patternsCondition, RequestMethodsRequestCondition requestMethodsRequestCondition) {
        this.patternsCondition = patternsCondition;
        this.requestMethodsRequestCondition = requestMethodsRequestCondition;
    }

    /**
     * 将两个 RequestCondition 对象结合
     */
    @Override
    public RequestMappingInfo combine(RequestMappingInfo other) {
        PatternsRequestCondition patternsRequestCondition = this.patternsCondition.combine(other.patternsCondition);
        RequestMethodsRequestCondition requestMethodsRequestCondition = this.requestMethodsRequestCondition.combine(other.requestMethodsRequestCondition);

        return new RequestMappingInfo(patternsRequestCondition, requestMethodsRequestCondition);
    }

    /**
     * 检查此条件是否与给定请求匹配，并返回潜在的新请求条件，其中包含适合当前请求的内容。
     * <p>
     * 不匹配返回 null
     */
    @Override
    public RequestMappingInfo getMatchingCondition(HttpServletRequest request) {
        PatternsRequestCondition pattern = this.patternsCondition.getMatchingCondition(request);
        if (ObjectUtil.isNull(pattern)) {
            return null;
        }

        RequestMethodsRequestCondition requestMethods = this.requestMethodsRequestCondition.getMatchingCondition(request);
        if (ObjectUtil.isNull(requestMethods)) {
            return null;
        }

        return new RequestMappingInfo(pattern, requestMethods);
    }

    public static Builder paths(String... paths) {
        return new Builder(paths);
    }

    public PatternsRequestCondition getPatternsCondition() {
        return patternsCondition;
    }

    public RequestMethodsRequestCondition getRequestMethodsRequestCondition() {
        return requestMethodsRequestCondition;
    }

    public static class Builder {

        private String[] paths;

        private RequestMethod[] methods;

        public Builder(String... paths) {
            this.paths = paths;
        }

        public Builder paths(String... paths) {
            this.paths = paths;
            return this;
        }

        public Builder methods(RequestMethod... methods) {
            this.methods = methods;
            return this;
        }

        public RequestMappingInfo build() {

            PatternsRequestCondition patternsCondition = paths != null ? new PatternsRequestCondition(this.paths) : new PatternsRequestCondition();
            RequestMethodsRequestCondition requestMethodsRequestCondition = methods != null ? new RequestMethodsRequestCondition(methods) : new RequestMethodsRequestCondition();

            return new RequestMappingInfo(patternsCondition, requestMethodsRequestCondition);
        }
    }
}
