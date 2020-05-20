package cn.x5456.summer.rpc;

import cn.x5456.summer.rpc.common.Node;

/**
 * Invoker. (API/SPI, Prototype, ThreadSafe)
 */
public interface Invoker<T> extends Node {

    /**
     * get service interface.
     *
     * @return service interface.
     */
    Class<T> getInterface();

    /**
     * invoke.
     */
    Result invoke(Invocation invocation);

}