package com.raptor.gulimall.seckill.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * @author raptor
 * @description HelloSchedule
 * @date 2021/12/9 20:08
 */
@Component
@EnableScheduling
//开启异步任务
@EnableAsync
@Slf4j
public class HelloSchedule {


    /**
     * 1. Spring的corn由65位组成，不予许第七位的年
     * 2. 在周几的位置，1-7代表周一到周日，MON-SUN
     * 3. 定时任务不应该阻塞。默认是阻塞的
     *  1） 可以让业务运行以异步的方式，提交到自己的线程池
     *        CompletableFuture.runAsync(()->{
     *         }.executor);
     *  2) 支持异步线程池；设置spring.task.schedule.pool.size=5
     *  3) 异步任务  @EnableAsync
     *
     */
    @Async
    @Scheduled(cron = "* * * ? * 4")
    public void hello() throws InterruptedException {
        log.info("hello ...");
        Thread.sleep(3000);
    }

}
