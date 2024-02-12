package com.jianhongl.fresh.mock.rpcclient.enhance;

import java.util.concurrent.CompletableFuture;

/**
 * 用于增强 CompletableFuture 的功能. 通过增加一个名字属性, 用于标识这个 CompletableFuture 的名字
 *
 * @param <T> 泛型参数
 */
public class CompletableFutureEx<T> extends CompletableFuture<T> {
    private String name;

    public CompletableFutureEx(String name) {
        super();
        this.name = name;
    }


    /**
     * Returns a string identifying this CompletableFuture, as well as
     * its completion state.  The state, in brackets, contains the
     * String {@code "Completed Normally"} or the String {@code
     * "Completed Exceptionally"}, or the String {@code "Not
     * completed"} followed by the number of CompletableFutures
     * dependent upon its completion, if any.
     *
     * @return a string identifying this CompletableFuture, as well as its state
     */
    @Override
    public String toString() {
        return "[ futureName: " + name + "] " + super.toString();
    }
}
