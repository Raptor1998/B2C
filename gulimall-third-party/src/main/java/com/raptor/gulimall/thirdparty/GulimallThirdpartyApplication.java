package com.raptor.gulimall.thirdparty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author raptor
 * @description GulimallThirdpartyApplication
 * @date 2021/10/15 21:10
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GulimallThirdpartyApplication {
    public static void main(String[] args) {
        SpringApplication.run(GulimallThirdpartyApplication.class, args);
    }
}
