package com.booking.web;

import java.util.Arrays;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.booking.domain.Comment;
import com.booking.domain.Hotel;
import com.booking.domain.Order;
import com.booking.dto.CommentQueryDTO;
import com.booking.enums.CommentTypeEnum;
import com.booking.enums.OrderStatusEnum;
import com.booking.service.CommentService;
import com.booking.service.HotelService;
import com.booking.service.OrderService;
import com.booking.utils.CopyPropertiesUtil;
import com.booking.utils.ResponseEntity;
import com.booking.utils.STablePageRequest;

@RestController
@RequestMapping(value = "/commentManage")
public class CommentController {
	@Autowired
	CommentService commentService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	HotelService hotelService;
	
	/**
     * 获取一页评论
     *
     * @param pageable
     * @return
     */
    @GetMapping
    public ResponseEntity<Page<Comment>> getHotelPage(STablePageRequest pageable, CommentQueryDTO commentQueryDTO) {
        if (StringUtils.isBlank(pageable.getSortField())) {
            pageable.setSortField("cid");
        }
        Page<Comment> page = Page.empty(pageable.getPageable());
        page = commentService.findAll(CommentQueryDTO.getSpecification(commentQueryDTO), pageable.getPageable());
        return ResponseEntity.ofSuccess().status(HttpStatus.OK).data(page);
    }
    
    /**
     * 根据id删除评论信息
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity deleteHotelById(@PathVariable("id") Long id) {
        commentService.deleteById(id);
        return ResponseEntity.ofSuccess().status(HttpStatus.OK);
    }

    /**
     * 批量删除评论信息
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public ResponseEntity deleteHotelByIds(Long[] ids) {
        commentService.deleteAll(Arrays.asList(ids));
        return ResponseEntity.ofSuccess().status(HttpStatus.OK);
    }
    /**
     * 回复评论
     * @param comment
     * @return
     */
    @PutMapping("/{cid}")
    public ResponseEntity reply(@PathVariable("cid") Long cid, @RequestBody Comment comment) {
    	Comment target = commentService.findById(cid);
        BeanUtils.copyProperties(comment, target, CopyPropertiesUtil.getNullPropertyNames(comment));
        commentService.save(target);
    	return ResponseEntity.ofSuccess().status(HttpStatus.OK);
    }
    
    /**
     * 添加评论
     * @param comment
     * @return
     */
    @PostMapping("/{oid}")
    public ResponseEntity evaluate(@PathVariable("oid") Long oid, @RequestBody Comment comment) {
    	Order order = orderService.findById(oid);
    	Hotel hotel = order.getHotel();
    	comment.setDate(new Date());
    	comment.setOrder(order);
    	comment.setHotel(hotel);
    	comment.setUser(order.getUser());
    	if(comment.getRate() >= 3.5) {
    		comment.setType(CommentTypeEnum.PRAISE);
    	}else if(comment.getRate() >= 2) {
    		comment.setType(CommentTypeEnum.AVERAGE);
    	}else {
    		comment.setType(CommentTypeEnum.CRITICIZE);
    	}
    	Float hotelOldRate = hotel.getRate();
    	Float hotelNewRate = (hotelOldRate*commentService.count() + comment.getRate()) / (commentService.count() + 1);
        hotel.setRate(hotelNewRate);
        order.setStatus(OrderStatusEnum.REVIEWED);
    	commentService.save(comment);
    	orderService.save(order);//更新订单状态为已评价
    	hotelService.save(hotel);//更新酒店评分
    	return ResponseEntity.ofSuccess().status(HttpStatus.OK);
    }
}
