package com.booking.dto;

import com.booking.enums.PayTypeEnum;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class OrderConfirmDTO {
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    private Integer count;
    private String checkInPerson;
    private String telephone;
    private String remark;
    private PayTypeEnum payType;
    private Long rid;
    private Double totalPrice;
}
