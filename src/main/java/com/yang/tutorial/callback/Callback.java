package com.yang.tutorial.callback;

/**
 * @author yangzijing
 */
public interface Callback<T> {

    /**
     * 具体实现
     * @param t
     */
    public void apply(T t);

}
