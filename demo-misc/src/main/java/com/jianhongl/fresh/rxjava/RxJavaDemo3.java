package com.jianhongl.fresh.rxjava;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RxJavaDemo3 {

    public static void main(String[] args) throws InterruptedException {

        /**
         * 改变 subscribeOn() 的位置, 并不会影响整体的执行线程.
         */
        String[] str = {"A", "B", "C"};
        Observable.fromArray(str)
                  .map(s -> {
                      System.out.println(Thread.currentThread().getName() + "  map : " + s);
                      return s + " Hi";
                  })
                  .subscribeOn(Schedulers.io()) //
                  .subscribe(s -> System.out.println(Thread.currentThread().getName() + "  call : " + s));

        // sleep here
        Thread.sleep(1000); // 等待1秒钟
    }

}
