package cn.x5456.summer.web.servlet.mvc.condition;

import cn.hutool.core.util.ObjectUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author yujx
 * @date 2020/04/24 13:48
 */
public final class PatternsRequestCondition implements RequestCondition<PatternsRequestCondition> {

    private final Set<String> patterns = new HashSet<>();

    public PatternsRequestCondition(String... paths) {
        this(Arrays.asList(paths));
    }

    public PatternsRequestCondition(Collection<String> patterns) {
        this.patterns.addAll(patterns);
    }

    public Set<String> getPatterns() {
        return this.patterns;
    }

    /**
     * 将两个 RequestCondition 对象结合
     */
    @Override
    public PatternsRequestCondition combine(PatternsRequestCondition other) {
        Set<String> result = new HashSet<>();

        for (String classPattern : other.getPatterns()) {
            for (String childPattern : this.getPatterns()) {
                result.add(classPattern + childPattern);
            }
        }

        return new PatternsRequestCondition(result);
    }

    /**
     * 检查此条件是否与给定请求匹配，并返回潜在的新请求条件，其中包含适合当前请求的内容。
     * <p>
     * 不匹配返回 null
     */
    @Override
    public PatternsRequestCondition getMatchingCondition(HttpServletRequest request) {
        String lookupPath = request.getServletPath();

        Set<String> matches = new HashSet<>();
        for (String pattern : this.patterns) {
            String match = this.getMatchingPattern(pattern, lookupPath);
            if (match != null) {
                matches.add(match);
            }
        }

        return matches.isEmpty() ? null : new PatternsRequestCondition(matches);
    }

    private String getMatchingPattern(String pattern, String lookupPath) {
        if (ObjectUtil.equal(pattern, lookupPath)) {
            return pattern;
        }

        // 解析 {} 的情况
        // TODO: 2020/4/24 我实在不想写啊

        return null;
    }
}
