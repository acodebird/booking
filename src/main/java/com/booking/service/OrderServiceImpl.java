package com.booking.service;

import com.booking.domain.Order;
import com.booking.domain.Room;
import com.booking.enums.OrderStatusEnum;
import com.booking.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RoomService roomService;

    /**
     * 通过id查找订单
     *
     * @param id
     * @return
     */
    @Override
    public Order findById(Long id) {
        Order order = orderRepository.findById(id).get();
        isCancel(order);
//        order.getUser().setSalt(null);
//        order.getUser().setUpassword(null);
        return order;
    }

    /**
     * 更新或新增订单
     *
     * @param order
     */
    @Override
    @Transactional
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    /**
     * 获取所有订单
     *
     * @return
     */
    @Override
    public List<Order> findAll() {
        return (List<Order>) orderRepository.findAll();
    }

    /**
     * 查询一页订单
     *
     * @param pageable
     * @return
     */
    @Override
    public Page<Order> findAll(Specification<Order> spec, Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(spec, pageable);
        //清空用户的密码和盐
        orders.forEach(order -> {
            isCancel(order);
//            order.getUser().setSalt(null);
//            order.getUser().setUpassword(null);
        });

        return orders;
    }

    /**
     * 为某一个用户查询其所有订单
     *
     * @param spec
     * @return
     */
    @Override
    public List<Order> findAll(Specification<Order> spec) {
        List<Order> orders = orderRepository.findAll(spec);
        //清空用户的密码和盐
        orders.forEach(order -> {
            isCancel(order);
//            order.getUser().setSalt(null);
//            order.getUser().setUpassword(null);
        });

        return orders;
    }

    /**
     * 根据id删除一条订单
     *
     * @param id
     */
    @Override
    @Transactional
    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }

    /**
     * 根据id查找一批订单
     *
     * @param ids
     * @return
     */
    @Override
    public List<Order> findAllById(List<Long> ids) {
        return (List<Order>) orderRepository.findAllById(ids);
    }

    /**
     * 批量删除订单
     *
     * @param orders
     */
    @Override
    @Transactional
    public void deleteAll(List<Order> orders) {
        orderRepository.deleteAll(orders);
    }

    /**
     * 判断订单是否需要取消
     *
     * @param order
     */
    @Override
    @Transactional
    public void isCancel(Order order) {
        //满足两个条件则取消订单：1、订单状态为“未付款(UNPAY)”；2、订单的过期时间小于当前时间
        if (order != null && order.getStatus() == OrderStatusEnum.UNPAY && order.getCancelTime().compareTo(new Date()) <= 0) {
            order.setStatus(OrderStatusEnum.CANCEL);
            orderRepository.save(order);
            //该订单的房间库存量加上房间的预订数量
            Room room = order.getRoom();
            room.setStock(room.getStock() + order.getCount());
            roomService.save(room);
        }
    }
}
