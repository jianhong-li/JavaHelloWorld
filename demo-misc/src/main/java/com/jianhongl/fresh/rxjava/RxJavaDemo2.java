package com.jianhongl.fresh.rxjava;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RxJavaDemo2 {

    public static void main(String[] args) throws InterruptedException {

        /**
         * 改变 observeOn() 的位置,只有后续的操作才使用了 io 线程处理.
         */
        String[] str = {"A", "B", "C"};
        Observable.fromArray(str)
                  .map(s -> {
                      System.out.println(Thread.currentThread().getName() + "  map : " + s);
                      return s + " Hi";
                  })
                  .observeOn(Schedulers.io()) // 改变位置到此.  只有后续的操作才使用了 io 线程处理.
                  .subscribe(s -> System.out.println(Thread.currentThread().getName() + "  call : " + s));

        // sleep here
        Thread.sleep(1000); // 等待1秒钟
    }

}
