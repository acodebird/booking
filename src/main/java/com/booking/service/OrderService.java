package com.booking.service;

import com.booking.domain.Order;
import com.booking.enums.OrderStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface OrderService {
    /**
     * 根据id查询一条订单
     *
     * @param id
     * @return
     */
    Order findById(Long id);

    /**
     * 更新或新增订单
     *
     * @param order
     */
    Order save(Order order);

    /**
     * 获取所有订单
     *
     * @return
     */
    List<Order> findAll();

    /**
     * 条件查询一页订单
     *
     * @param spec
     * @param pageable
     * @return
     */
    Page<Order> findAll(Specification<Order> spec, Pageable pageable);

    /**
     * 为某一个用户查询其所有订单
     *
     * @param spec
     * @return
     */
    List<Order> findAll(Specification<Order> spec);

    /**
     * 根据id删除一条订单
     *
     * @param id
     */
    void deleteById(Long id);

    /**
     * 根据id查找一批订单
     *
     * @param ids
     * @return
     */
    List<Order> findAllById(List<Long> ids);

    /**
     * 批量删除订单
     *
     * @param orders
     */
    void deleteAll(List<Order> orders);

    /**
     * 判断订单是否需要取消
     *
     * @param order
     */
    void isCancel(Order order);
}
