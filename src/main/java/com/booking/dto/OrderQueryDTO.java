package com.booking.dto;

import com.booking.domain.Order;
import com.booking.enums.OrderStatusEnum;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 接口类：用于订单的条件查询
 */
@Data
public class OrderQueryDTO implements Serializable {
    private String hName;
    private Integer maxPrice;
    private Integer minPrice;
    private Long oid;
    private OrderStatusEnum orderStatus;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    private String uName;

    @SuppressWarnings({"serial"})
    public static Specification<Order> getWhereClause(final OrderQueryDTO orderQueryDTO) {
        return new Specification<Order>() {
            @Override
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicate = new ArrayList<>();
                //拼接酒店名称
                if (StringUtils.isNotBlank(orderQueryDTO.getHName()) && !"ALL".equals(orderQueryDTO.getHName())) {
                    predicate.add(criteriaBuilder.equal(root.get("hotel").get("hname").as(String.class),
                            orderQueryDTO.getHName()));
                }
                //拼接价格区间
                if (null != orderQueryDTO.getMaxPrice() || null != orderQueryDTO.getMinPrice()) {
                    if (null != orderQueryDTO.getMinPrice() && null == orderQueryDTO.getMaxPrice()) {
                        //最低价格不为空，最高价格为空，即totalPrice >= minPrice
                        predicate.add(criteriaBuilder.greaterThanOrEqualTo(root.get("totalPrice").as(Integer.class),
                                orderQueryDTO.getMinPrice()));
                    } else if (null == orderQueryDTO.getMinPrice() && null != orderQueryDTO.getMaxPrice()) {
                        //最低价格为空，最高价格不为空，即totalPrice <= maxPrice
                        predicate.add(criteriaBuilder.lessThanOrEqualTo(root.get("totalPrice").as(Integer.class),
                                orderQueryDTO.getMaxPrice()));
                    } else {
                        //最低价格不为空，最高价格也不为空，即minPrice <= totalPrice <= maxPrice
                        predicate.add(criteriaBuilder.between(root.get("totalPrice").as(Integer.class),
                                orderQueryDTO.getMinPrice(), orderQueryDTO.getMaxPrice()));
                    }
                }
                //拼接订单状态
                if (null != orderQueryDTO.getOrderStatus() && "ALL" != orderQueryDTO.getOrderStatus().name()) {
                    predicate.add(criteriaBuilder.equal(root.get("status").as(OrderStatusEnum.class),
                            orderQueryDTO.getOrderStatus()));
                }
                //拼接入住时间
                if (null != orderQueryDTO.getStartTime()) {
                    predicate.add(criteriaBuilder.greaterThanOrEqualTo(root.get("startTime").as(Date.class),
                            orderQueryDTO.getStartTime()));
                }
                //拼接用户名称
                if (StringUtils.isNotBlank(orderQueryDTO.getUName())) {
                    predicate.add(criteriaBuilder.like(root.get("checkInPerson").as(String.class),
                            "%" + orderQueryDTO.getUName() + "%"));
                }
                //拼接订单编号
                if (null != orderQueryDTO.getOid()) {
                    predicate.add(criteriaBuilder.equal(root.get("oid").as(Integer.class),
                            orderQueryDTO.getOid()));
                }

                Predicate[] predicates = new Predicate[predicate.size()];
                return criteriaQuery.where(predicate.toArray(predicates)).getRestriction();
            }
        };
    }
}
