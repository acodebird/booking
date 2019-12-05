package com.booking.repository;

import com.booking.domain.Order;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaSpecificationExecutor<Order>, PagingAndSortingRepository<Order, Long> {
}
