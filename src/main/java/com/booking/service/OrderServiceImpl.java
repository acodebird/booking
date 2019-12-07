package com.booking.service;

import com.booking.domain.Order;
import com.booking.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    /**
     * 更新或新增订单
     * @param order
     */
    @Override
    public void save(Order order) {
        orderRepository.save(order);
    }

    /**
     * 查询一页订单
     * @param pageable
     * @return
     */
    @Override
    public Page<Order> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    /**
     * 根据id删除一条订单
     * @param id
     */
    @Override
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
    public void deleteAll(List<Order> orders) {
        orderRepository.deleteAll(orders);
    }
}
