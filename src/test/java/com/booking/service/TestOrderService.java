package com.booking.service;

import com.booking.domain.Order;
import com.booking.enums.OrderStatusEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
public class TestOrderService {
    @Autowired
    private OrderService orderService;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private UserService userService;

    @Test
    public void testSaveOrder() throws InterruptedException {
        for (int i = 1; i <= 50; i++) {
            Order order = new Order();
            order.setCount(2);
            order.setPrice(129.9);
            order.setTotalprice(order.getCount() * order.getPrice());
            order.setCreateTime(new Date());
            order.setStartTime(new Date());
            Thread.sleep(3000L);
            order.setEndTime(new Date());
            if (i % 4 == 0) order.setStatus(OrderStatusEnum.UNPAY);
            else if (i % 3 == 0) order.setStatus(OrderStatusEnum.UNUSE);
            else if (i % 2 == 0) order.setStatus(OrderStatusEnum.SUCCESS);
            else order.setStatus(OrderStatusEnum.CANCEL);
            order.setHotel(hotelService.findById(45L));
            order.setRoom(roomService.findById(10L));
            order.setUser(userService.getUserById(2L));
            orderService.save(order);
        }
    }
}
