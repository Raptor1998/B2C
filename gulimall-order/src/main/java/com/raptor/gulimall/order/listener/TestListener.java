package com.raptor.gulimall.order.listener;

import com.rabbitmq.client.Channel;
import com.raptor.gulimall.order.entity.OrderEntity;
import com.raptor.gulimall.order.entity.OrderReturnReasonEntity;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author raptor
 * @description TestListener
 * @date 2021/11/29 20:52
 */
@RabbitListener(queues = {"hello.java"})
@Service
public class TestListener {
    /**
     * @param content T<发送消息的类型>
     * @parammessage 原生消息详细信息， 头+体
     * @paramchannel 当前传输数据的通道
     */
    @RabbitHandler
    public void receiveMessage(Message message, OrderReturnReasonEntity content, Channel channel) {
        System.out.println(content);
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            if (deliveryTag%2==0){
                channel.basicAck(deliveryTag, false);
                System.out.println("签收了货物" + deliveryTag);
            }else {
                System.out.println("没有签收"+deliveryTag);
            }

        } catch (IOException e) {
            // 网络中断
            e.printStackTrace();
        }
    }

    @RabbitHandler
    public void receiveMessage2(Message message, OrderEntity orderEntity, Channel channel) {
        System.out.println(orderEntity);
    }
}
