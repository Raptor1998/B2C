package com.raptor.gulimall.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author raptor
 * @description GulimallCartApplication
 * @date 2021/11/20 21:15
 */
@EnableRedisHttpSession
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class GulimallCartApplication {
    public static void main(String[] args) {
        SpringApplication.run(GulimallCartApplication.class, args);
    }
}
