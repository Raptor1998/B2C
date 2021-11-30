package com.raptor.gulimall.order.controller;

import com.raptor.gulimall.order.entity.OrderEntity;
import com.raptor.gulimall.order.entity.OrderReturnReasonEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

/**
 * @author raptor
 * @description MqTest
 * @date 2021/11/29 20:49
 */
@Slf4j
@RestController
public class MqTest {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @GetMapping("sendMq")
    public String contextLoads(@RequestParam(value = "num", defaultValue = "10") Integer num) {

        for (int i = 0; i < num; i++) {
            if (i % 2 == 0) {
                OrderReturnReasonEntity returnReasonEntity = new OrderReturnReasonEntity();
                returnReasonEntity.setId(1L);
                returnReasonEntity.setCreateTime(new Date());
                returnReasonEntity.setName("哈哈哈22");
                rabbitTemplate.convertAndSend("amq.direct", "hello.java", returnReasonEntity,new CorrelationData(UUID.randomUUID().toString()));
            } else {
                OrderEntity orderEntity = new OrderEntity();
                orderEntity.setOrderSn(UUID.randomUUID().toString());
                rabbitTemplate.convertAndSend("amq.direct", "hellosss.java", orderEntity,new CorrelationData(UUID.randomUUID().toString()));
            }
        }

        log.info("发送完成");
        return "OK";
    }


}
