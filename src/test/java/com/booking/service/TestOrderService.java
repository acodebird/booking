package com.booking.service;

import com.booking.domain.Order;
import com.booking.enums.OrderStatusEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

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
    public void testSaveOrder() {
        for (int j = 0; j < 5; j++) {
            for (int i = 1; i <= 5; i++) {
                Order order = new Order();
                order.setCount(2);
                order.setPrice(129.9);
                order.setTotalPrice(order.getCount() * order.getPrice());
                order.setCreateTime(new Date());
                order.setStartTime(new Date());
                order.setEndTime(new Date());
                if (i % 4 == 0) order.setStatus(OrderStatusEnum.UNPAY);
                else if (i % 3 == 0) order.setStatus(OrderStatusEnum.UNUSE);
                else if (i % 2 == 0) order.setStatus(OrderStatusEnum.SUCCESS);
                else order.setStatus(OrderStatusEnum.CANCEL);
                order.setHotel(hotelService.findById(2L));
                order.setRoom(roomService.findById(10L));
                order.setUser(userService.getUserById(90L + j));
                order.setRemark("无烟房,外窗");
                order.setCheckInPerson(order.getUser().getUname());
                orderService.save(order);
            }
        }
    }

    @Test
    public void testGenarateCancelTime() {
        List<Order> orders = orderService.findAll();
        orders.forEach(order -> {
            long cancelTime = order.getCreateTime().getTime() + 30 * 60 * 1000;
            order.setCancelTime(new Date(cancelTime));
            orderService.save(order);
        });
    }
}
