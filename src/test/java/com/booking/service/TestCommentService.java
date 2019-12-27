package com.booking.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.booking.domain.Comment;
import com.booking.domain.Order;
import com.booking.enums.CommentTypeEnum;

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
        List<Comment> comments=new ArrayList<Comment>();
        for(int i=0;i<30;i++){
            Order order=orderService.findById(1L+i);
            Comment comment=new Comment();
            comment.setContent("这是我住过的最差劲的酒店，一晚上将近1000的房子，到了晚上七点多外面就开始唱歌，唱到11点才结束，我想休息一下都不行，还说这个没办法解决。我告诉你们怎么解决，把所有的城景套房全部下架，你们既然定了这么高的价格，就要给消费者相对应的服务和体验。 洗手间里水管是漏的，一用洗手间就满地的水。热水也要放很久很久水才有，真不知道这酒店凭什么是这个价格。真牛逼"+i);
            comment.setDate(new Date());
            comment.setOrder(order);
            comment.setRate(5f);
            comment.setType(CommentTypeEnum.AVERAGE);
            comment.setUser(order.getUser());
            comment.setHotel(order.getHotel());
            comments.add(comment);
        }
        commentService.save(comments);
    }
}
