package com.booking.repository;

import com.booking.domain.Order;
import com.booking.enums.OrderStatusEnum;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaSpecificationExecutor<Order>, PagingAndSortingRepository<Order, Long> {
}
