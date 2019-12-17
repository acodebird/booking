package com.booking.service;

import com.booking.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    /**
     * 根据id查询一条订单
     * @param id
     * @return
     */
    Order findById(Long id);

    /**
     * 更新或新增订单
     * @param order
     */
    void save(Order order);

    /**
     * 查询一页订单
     * @param pageable
     * @return
     */
    Page<Order> findAll(Pageable pageable);

    /**
     * 根据id删除一条订单
     * @param id
     */
    void deleteById(Long id);

    /**
     * 根据id查找一批订单
     * @param ids
     * @return
     */
    List<Order> findAllById(List<Long> ids);

    /**
     * 批量删除订单
     * @param orders
     */
    void deleteAll(List<Order> orders);
}
