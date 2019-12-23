package com.booking.service;

import com.booking.domain.Order;
import com.booking.domain.User;
import com.booking.dto.UserQueryDTO;
import com.booking.enums.OrderStatusEnum;

import com.booking.utils.STablePageRequest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestUserService {
    @Autowired
    UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private RoomService roomService;

    @Test
    public void saveUsers(){
        String p="0000000000";
        for(int i=0;i<100;i++){
            User user=new User();
            user.setSalt("salt_"+i);
            user.setUpassword("password_"+i);
            user.setEmail(i+"_@example.com");
            user.setEnable((1==(i%2)?false:true));
            user.setIcon("/upload/user/avatar/default_avatar.jpeg");
            user.setTelephone((String.valueOf(i)+p).substring(0,11));
            user.setType(i%2);
            user.setUname("name_"+i);
            userService.save(user);
        }
    }
    @Test
    public void findUsers(){
        STablePageRequest pageable=new STablePageRequest();
        UserQueryDTO queryDTO=new UserQueryDTO();
        Page<User> page = userService.findAll(queryDTO.getWhereClause(queryDTO),pageable.getUserPageable());
    }
    @Test
    public void findOrderByUser(){
        UserQueryDTO queryDTO=new UserQueryDTO();
        List<Long> uids=queryDTO.getUids();
        uids.add(1L);
        uids.add(2L);
        queryDTO.setUids(uids);
        List<Order> orders= userService.findAllOrder(queryDTO.getOrderSepcByUser(queryDTO));
        for(Order o : orders){
            System.out.println(o);
        }
    }

    @Test
    public void testSaveOrder() {
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
            order.setUser(userService.findById(3L));
            order.setRemark("无烟房,外窗");
            order.setCheckInPerson(order.getUser().getUname());
            orderService.save(order);
        }
    }
}
