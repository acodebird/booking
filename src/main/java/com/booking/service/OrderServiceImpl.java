package com.booking.service;

import com.booking.domain.Order;
import com.booking.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Order findById(Long id) {
        return orderRepository.findById(id).get();
    }

    /**
     * 更新或新增订单
     * @param order
     */
    @Override
    @Transactional
    public void save(Order order) {
        orderRepository.save(order);
    }

    /**
     * 查询一页订单
     * @param pageable
     * @return
     */
    @Override
    public Page<Order> findAll(Specification<Order> spec, Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(spec, pageable);
        //清空用户的密码和盐
        orders.forEach(order -> {
            order.getUser().setSalt(null);
            order.getUser().setUpassword(null);
        });

        return orders;
    }

    /**
     * 根据id删除一条订单
     * @param id
     */
    @Override
    @Transactional
    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }

    /**
     * 根据id查找一批订单
     * @param ids
     * @return
     */
    @Override
    public List<Order> findAllById(List<Long> ids) {
        return (List<Order>) orderRepository.findAllById(ids);
    }

    /**
     * 批量删除订单
     * @param orders
     */
    @Override
    @Transactional
    public void deleteAll(List<Order> orders) {
        orderRepository.deleteAll(orders);
    }
}
