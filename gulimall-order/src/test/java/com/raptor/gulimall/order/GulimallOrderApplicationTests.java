package com.raptor.gulimall.order;

import com.raptor.gulimall.order.entity.OrderReturnReasonEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
class GulimallOrderApplicationTests {


    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    void contextLoads() {

        OrderReturnReasonEntity returnReasonEntity = new OrderReturnReasonEntity();
        returnReasonEntity.setId(1L);
        returnReasonEntity.setCreateTime(new Date());
        returnReasonEntity.setName("哈哈哈22");

        String msg = "Hello world !";

        rabbitTemplate.convertAndSend("amq.direct","hello.java",returnReasonEntity);
        log.info("发送完成");
    }


}
