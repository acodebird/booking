package com.booking.dto;

import com.booking.domain.Hotel;
import com.booking.domain.Room;
import com.booking.enums.OrderStatusEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class OrderDTO implements Serializable {
    private Hotel hotel;
    private Room room;
    private String checkInPerson;
    private String remark;
    private OrderStatusEnum status;
    private Date startTime;
    private Date endTime;
    private Long oid;
    private Double price;
    private Double totalPrice;
    private Integer count;
}
