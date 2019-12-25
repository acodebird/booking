package com.booking.utils;

import com.booking.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AutoCancelOrderTask {
    @Autowired
    private OrderService orderService;

    /**
     * 每分钟处理一次所有订单的状态，把未付款且超时的订单状态改为取消
     */
    @Scheduled(fixedRate = 1000 * 60)
    public void cancelOrder() {
        orderService.findAll().forEach(order -> orderService.isCancel(order));
        System.out.println("已执行一次取消订单状态的任务");
    }
}
