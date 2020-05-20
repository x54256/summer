package cn.x5456.summer.rpc;

import java.io.Serializable;

/**
 * @author yujx
 * @date 2020/05/20 14:39
 */
public class RpcResult implements Result, Serializable {

    private Object result;

    private Throwable exception;

    public RpcResult(Object result) {
        this.result = result;
    }

    public RpcResult(Throwable exception) {
        this.exception = exception;
    }

    @Override
    public Object getValue() {
        return result;
    }

    @Override
    public Throwable getException() {
        return exception;
    }

    @Override
    public boolean hasException() {
        return exception != null;
    }

    @Override
    public Object recreate() {
        if (hasException()) {
            return result;
        }

       throw new RuntimeException(exception);
    }

    @Override
    public String toString() {
        return "RpcResult [result=" + result + ", exception=" + exception + "]";
    }
}
