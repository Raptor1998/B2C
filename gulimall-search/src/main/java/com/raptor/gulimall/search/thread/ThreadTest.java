package com.raptor.gulimall.search.thread;

import io.lettuce.core.protocol.CompleteableCommand;
import io.netty.util.NetUtil;
import io.netty.util.concurrent.CompleteFuture;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.util.ObjectUtils;

import java.util.concurrent.*;

/**
 * @author raptor
 * @description ThreadTest
 * @date 2021/12/1 16:10
 */
public class ThreadTest {

    public static ExecutorService service = Executors.newFixedThreadPool(10);

    public void threadTest(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("main....start");

        /**
         * 集成Thread
         * 实现Runnable接口
         * 实现Callable接口+futureTask
         * 线程池
         */
        //Thread01 thread01 = new Thread01();
        //thread01.start();

        //Runnable01 runnable01 = new Runnable01();
        //new Thread(runnable01).start();


        //FutureTask<Integer> futureTask = new FutureTask<Integer>(new Callable01());
        //new Thread(futureTask).start();
        ////阻塞等待
        //Integer integer = futureTask.get();
        //System.out.println(integer);

        //应该将所有的异步任务都交给线程池执行

        /**
         * int corePoolSize,  核心线程数，线程池创建号以后就准备就绪的线程数量，就等待接受异步任务去执行
         * 只要线程池不销毁，就一直存在   除非奢姿了这个allowCoreThreadTimeOut
         * int maximumPoolSize,  最大的线程数量  控制资源并发
         * long keepAliveTime,  存活时间，当正在运行线程数量大于核心数量，回释放空闲的线程，
         * 只要线程空闲大于执行的存活时间，释放的线程等于maximumPoolSize-corePoolSize
         * TimeUnit unit, 时间单位
         * BlockingQueue<Runnable> workQueue, 阻塞队列 如果任务很多，将多余的任务放在队列里，有现成空闲就回去队列里取新的任务
         * ThreadFactory threadFactory,  线程创建工厂，
         * RejectedExecutionHandler handler   如果队列满了，按照指定的拒绝策略，拒绝执行任务
         *
         *
         *
         * 工作顺序：
         * 1. 线程池创建号，准备好core的核心线程数量，准备接受任务
         * 2. 新的任务进来，用core准备好的空闲线程去执行
         *  （1）core满了，将再进来的任务放到阻塞队列中，空闲的core就会自己去阻塞队列获取任务
         *  （2）阻塞队列满了，就直接开新的线程执行，最大只能开到max指定的数量
         *  （3）max都执行好了，Max-core数量空闲的线程会在keepAliveTime指定的时间后自动销毁，最终保持到core大小
         *  （4）如果线程开到了max的数量，还有新任务进来，就会使用reject指定的拒绝策略进行处理
         * 3.所有的线程创建都是由指定的factory创建的
         *
         *  一个线程池 core 7，max 20 ，queue 50   100并发进来怎么分配
         *  先执行7个，后续50个请求进队列，然后创建13个新线程执行，
         *  现在执行了 7 + 13 个  50个在队列中   剩下30执行拒绝策略
         *
         *
         *  new LinkedBlockingDeque<>()  默认integer的最大值
         */

        ThreadPoolExecutor executor = new ThreadPoolExecutor(7,
                20,
                10,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(50),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());

        for (int i = 0; i < 100; i++) {
            executor.execute(new Thread(new Runnable() {
                @Override
                public void run() {

                    System.out.println("当前执行的线程是：" + Thread.currentThread().getName());
                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, String.valueOf(i)));
        }

        System.out.println("main....end");
        executor.shutdown();
    }

    public static class Thread01 extends Thread {
        @Override
        public void run() {
            System.out.println("当前线程：" + Thread.currentThread().getName());
            int i = 10 / 2;
            System.out.println("运行结果：" + i);
        }
    }

    public static class Runnable01 implements Runnable {
        @Override
        public void run() {
            System.out.println("当前线程：" + Thread.currentThread().getName());
            int i = 10 / 5;
            System.out.println("运行结果：" + i);
        }
    }

    public static class Callable01 implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            System.out.println("当前线程：" + Thread.currentThread().getName());
            int i = 10 / 1;
            System.out.println("运行结果：" + i);
            return i;
        }
    }


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("main....start");
        //CompletableFuture.runAsync(() -> {
        //    System.out.println("当前线程：" + Thread.currentThread().getName());
        //    int i = 10 / 5;
        //    System.out.println("运行结果：" + i);
        //}, service);


        //CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
        //    System.out.println("当前线程：" + Thread.currentThread().getName());
        //    int i = 10 / 0;
        //    try {
        //        Thread.sleep(1000);
        //    } catch (InterruptedException e) {
        //        e.printStackTrace();
        //    }
        //    System.out.println("运行结果：" + i);
        //    return i;
        //}, service).whenComplete((res, err) -> {
        //    // 虽然能得到异常信息，但是没法修改返回数据
        //    System.out.println("结果是：" + res);
        //    System.out.println("异常是：" + err);
        //}).exceptionally(throwable -> {
        //    //可以感知异常  同时返回默认值
        //    return 11;
        //});

        //CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
        //    System.out.println("当前线程：" + Thread.currentThread().getName());
        //    int i = 10 / 1;
        //    try {
        //        Thread.sleep(1000);
        //    } catch (InterruptedException e) {
        //        e.printStackTrace();
        //    }
        //    System.out.println("运行结果：" + i);
        //    return i;
        //}, service).handle((res, err) -> {
        //    if (ObjectUtils.isEmpty(res) == false) {
        //        return res * 2;
        //    }
        //    if (err != null) {
        //        return 0;
        //    }
        //    return 0;
        //});
        //System.out.println("future：" + future.get());


        /**
         * 线程串行化
         * 1. thenRun： 不能获取上一步的执行结果，无返回值
         *     thenRunAsync(() -> {
         *             System.out.println("任务2启动了");
         *         }, service)
         * 2. 能接收上一步的返回结果，但是无返回值
         *      thenAcceptAsync((res) -> {
         *             System.out.println("任务2启动了");
         *             System.out.println("res:" + res);
         *         }, service)
         * 3. 可以接受参数，有返回值
         *      thenApplyAsync((res) -> {
         *             System.out.println("任务2启动了");
         *             System.out.println("res:" + res);
         *             return "hello" + res;
         *         }, service)
         *
         */
        //CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
        //    System.out.println("当前线程：" + Thread.currentThread().getName());
        //    int i = 10 / 2;
        //    System.out.println("运行结果：" + i);
        //    return i;
        //}, service).thenApplyAsync((res) -> {
        //    System.out.println("任务2启动了");
        //    System.out.println("res:" + res);
        //    return "hello" + res;
        //}, service);


        //CompletableFuture<Integer> future01 = CompletableFuture.supplyAsync(() -> {
        //    System.out.println("任务1线程，当前线程：" + Thread.currentThread().getName());
        //    int i = 10 / 2;
        //    System.out.println("任务1运行结果：" + i);
        //    return i;
        //}, service);
        //
        //CompletableFuture<String> future02 = CompletableFuture.supplyAsync(() -> {
        //    System.out.println("任务2线程，当前线程：" + Thread.currentThread().getName());
        //    try {
        //        Thread.sleep(1000);
        //    } catch (InterruptedException e) {
        //        e.printStackTrace();
        //    }
        //    System.out.println("任务2运行结果：");
        //    return "hello";
        //}, service);

        //future01.runAfterBothAsync(future02, () -> {
        //    System.out.println("任务3开始");
        //}, service);

        //future01.thenAcceptBothAsync(future02, (f1, f2) -> {
        //    System.out.println("任务3开始  " + f1 + "  " + f2);
        //}, service);

        //CompletableFuture<String> future = future01.thenCombineAsync(future02, (f1, f2) -> {
        //    System.out.println("任务3开始  " + f1 + "  " + f2);
        //    return f1 + f2 + "哈哈";
        //}, service);

        //future01.applyToEitherAsync()   只要有一个完成即可执行


        CompletableFuture<String> futureImg = CompletableFuture.supplyAsync(() -> {
            System.out.println("查图片信息");
            return "hello.jpg";
        }, service);

        CompletableFuture<String> futureAttr = CompletableFuture.supplyAsync(() -> {

            try {
                Thread.sleep(3000);
                System.out.println("查商品属性");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "黑色128";
        }, service);

        CompletableFuture<String> futureDesc = CompletableFuture.supplyAsync(() -> {
            System.out.println("查商品介绍");
            return "小米";
        }, service);
        // 全部执行完成
        //CompletableFuture<Void> allOf = CompletableFuture.allOf(futureImg, futureAttr, futureDesc);
        //allOf.get();
        CompletableFuture<Object> anyOf = CompletableFuture.anyOf(futureImg, futureAttr, futureDesc);
        anyOf.get();
        System.out.println("成功："+ anyOf.get());
        System.out.println("main....end");
    }
}
