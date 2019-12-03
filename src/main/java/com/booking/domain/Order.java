package com.booking.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/*  `oid` int(11) NOT NULL AUTO_INCREMENT COMMENT '订单id',
  `count` int(11) NOT NULL COMMENT '订单房型数量',
  `price` double NOT NULL COMMENT '订单房型单价',
  `totalprice` double NOT NULL COMMENT '订单总价',
  `create_time` datetime NOT NULL COMMENT '订单创建时间',
  `start_time` datetime NOT NULL COMMENT '入住时间',
  `end_time` datetime NOT NULL COMMENT '离店时间',
  `status` int(11) NOT NULL COMMENT '订单状态（0表示未支付、1表示未完成、2表示完成）',
  `hid` int(11) NOT NULL COMMENT '订单所属酒店id',
  `rid` int(11) NOT NULL COMMENT '订单房型id',
  `uid` int(11) NOT NULL COMMENT '订单所属用户id',
 */

@Entity
@Data
@Table(name = "t_order")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long oid; //订单id
	private Integer count; //订单房型数量
	private Double price; //订单房型单价
	private Double totalprice; //订单总价

	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern="yyyy/MM/dd HH:mm:ss",timezone="GMT+8")
	@DateTimeFormat(pattern="yy/MM/dd HH:mm:ss")
	@Column(name = "create_time")
	private Date createTime; //订单创建时间

	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern="yyyy/MM/dd HH:mm:ss",timezone="GMT+8")
	@DateTimeFormat(pattern="yy/MM/dd HH:mm:ss")
	@Column(name = "start_time")
	private Date startTime; //入住时间

	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern="yyyy/MM/dd HH:mm:ss",timezone="GMT+8")
	@DateTimeFormat(pattern="yy/MM/dd HH:mm:ss")
	@Column(name = "end_time")
	private Date endTime; //离店时间

	private Integer status; //订单状态（0表示未支付、1表示未完成、2表示完成）
	
	@ManyToOne(cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
	@JoinColumn(name = "hid")
	private Hotel hotel; //订单所属酒店
	
	@ManyToOne(cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
	@JoinColumn(name = "rid")
	private Room room; //订单房型
	
	@ManyToOne(cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
	@JoinColumn(name = "uid")
	private User user; //订单所属用户
	
	//订单与评论建立双向一对一关系，由评论维护外键
	@OneToOne(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Comment comment;

	@Override
	public String toString() {
		return "Order [oid=" + oid + ", count=" + count + ", price=" + price + ", totalprice=" + totalprice
				+ ", createTime=" + createTime + ", startTime=" + startTime + ", endTime=" + endTime + ", status="
				+ status + "]";
	}
	
}
