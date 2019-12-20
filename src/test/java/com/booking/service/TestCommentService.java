package com.booking.service;

import com.booking.domain.Comment;
import com.booking.domain.Order;
import com.booking.enums.OrderStatusEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestCommentService {
    @Autowired
    UserService userService;
    @Autowired
    OrderService orderService;
    @Autowired
    HotelService hotelService;
    @Autowired
    CommentService commentService;

    @Test
    public void testSaveOrder() {
        List<Order> orders=new ArrayList<Order>();
        List<Comment> comments=new ArrayList<Comment>();
        for(int i=0;i<30;i++){
            Order order=orderService.findById(1L+i);
            Comment commont=new Comment();
            commont.setContent("comment_"+i);
            commont.setDate(new Date());
            commont.setHotel(order.getHotel());
            commont.setOrder(order);
            commont.setRate((float)i);
            commont.setType(i%3);
            commont.setUser(order.getUser());
            comments.add(commont);
        }
        commentService.save(comments);
    }
}
