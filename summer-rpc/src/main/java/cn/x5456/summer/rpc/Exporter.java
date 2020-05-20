package cn.x5456.summer.rpc;

public interface Exporter<T> {

    /**
     * get invoker.
     *
     * @return invoker
     */
    Invoker<T> getInvoker();

//    /**
//     * unexport.
//     * <p>
//     * <code>
//     * getInvoker().destroy();
//     * </code>
//            */
//    void unexport();

}