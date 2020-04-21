package cn.x5456.summer.beans.factory;

/**
 * @author yujx
 * @date 2020/04/16 09:37
 */
public interface DisposableBean {

	/**
	 * 由BeanFactory在销毁bean时调用。
	 */
	void destroy();

}
