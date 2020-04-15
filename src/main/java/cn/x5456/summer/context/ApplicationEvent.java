package cn.x5456.summer.context;

import java.util.EventObject;

/**
 * @author yujx
 * @date 2020/04/15 11:12
 */
public abstract class ApplicationEvent extends EventObject {

    private long timestamp;

    public ApplicationEvent(Object source) {
        super(source);
        timestamp = System.currentTimeMillis();
    }

    public long getTimestamp() {
        return timestamp;
    }
}
