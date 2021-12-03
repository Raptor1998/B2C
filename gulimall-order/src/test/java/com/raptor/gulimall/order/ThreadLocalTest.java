package com.raptor.gulimall.order;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author raptor
 * @description ThreadLocalTest
 * @date 2021/12/3 10:09
 */
public class ThreadLocalTest {

    static ThreadLocal<String> localVar = new ThreadLocal<>();

    public static ExecutorService service = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {

        System.out.println("开始测试。。。");

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + "开始");
                localVar.set("线程1的资源");
                System.out.println(Thread.currentThread().getName() + "资源设置完成");
                try {
                    Thread.sleep(10000);

                    System.out.println(Thread.currentThread().getName() + "获取的数据：" + localVar.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "线程1");

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + "开始");
                localVar.set("线程2的资源");
                System.out.println(Thread.currentThread().getName() + "资源设置完成");
                try {
                    //Thread.sleep(100);
                    System.out.println(Thread.currentThread().getName() + "获取的数据：" + localVar.get());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "线程2");


        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + "开始");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String s = localVar.get();
                System.out.println(Thread.currentThread().getName() + "获取的数据：" + localVar.get());
            }
        }, "线程3");

        service.execute(thread1);
        service.execute(thread2);
        service.execute(thread3);


        System.out.println("结束测试、、、、");

    }
}
