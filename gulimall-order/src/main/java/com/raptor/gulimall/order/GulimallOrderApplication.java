package com.raptor.gulimall.order;

import com.rabbitmq.client.AMQP;
import com.raptor.gulimall.order.entity.OrderEntity;
import com.raptor.gulimall.order.entity.OrderReturnReasonEntity;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@MapperScan("com.raptor.gulimall.order.dao")
@SpringBootApplication
@EnableDiscoveryClient
public class GulimallOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallOrderApplication.class, args);
    }

}
