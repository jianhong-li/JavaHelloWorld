package com.jianhongl.fresh.rxjava;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RxJavaDemo1 {

    /**
     * demo 代码出处: <a href="https://juejin.cn/post/6844903441660116999">RxJava 实用指南 - 01</a>
     * RxJava的调度器（Schedulers）为实际生产提供了5中解决方案，
     * 1) 如果需要大量IO操作，可以选择Schedulers.io()调度器，
     * 2) 如果需要进行密集计算，可以选择Schedulers.computation()，
     * 3) 如果只是想开一个线程做些事，可以选择Schedulers.newThread()，
     * 4) 借助于RxAndroid，我们还可以使用AndroidSchedulers.mainThread() 这个调度器将线程切换回主线程，选择了调度器后，使用一些方法可以将线程切换至调度器对应的线程中。
     * @param args
     */
    public static void main(String[] args) {

        /**
         * 多运行几次你会发现，几乎每次都没有输出！将observeOn(Schedulers.io())替换为subscribeOn(Schedulers.io())也同样如此！
         * 这是因为，当调用observeOn()或者subscribeOn()后，代码运行在子线程，
         * 这之后，子线程还没来得及调用map()和subscribe()，主线程就执行完了，因此，你很有可能是看不到运行结果的。
         *
         * 如果要看到输出结果，可以在代码的最后加上Thread.sleep(1000);，让主线程等待1秒钟，
         */
        String[] str = {"A", "B", "C"};
        Observable.fromArray(str)
                  .observeOn(Schedulers.io())
                  .map(s -> {
                      System.out.println(Thread.currentThread().getName() + "  map : " + s);
                      return s + " Hi";
                  })
                  .subscribe(s -> System.out.println(Thread.currentThread().getName() + "  call : " + s));

        // sleep here
    }


}
