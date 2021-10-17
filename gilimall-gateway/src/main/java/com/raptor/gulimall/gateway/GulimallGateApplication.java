package com.raptor.gulimall.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import javax.sql.DataSource;

/**
 * @author raptor
 * @description GulimallGateApplication
 * @date 2021/10/13 20:50
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
public class GulimallGateApplication {
    public static void main(String[] args) {
        SpringApplication.run(GulimallGateApplication.class);
    }
}
