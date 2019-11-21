package com.booking.domain;

import java.util.Date;

import javax.persistence.CascadeType;
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

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/*  `cid` int(11) NOT NULL AUTO_INCREMENT COMMENT '评论id',
  `content` varchar(255) NOT NULL COMMENT '评论内容',
  `rate` float NOT NULL COMMENT '评论等级',
  `type` int(11) NOT NULL COMMENT '评论类别（0表示差评、1表示中评、2表示好评）',
  `date` datetime NOT NULL COMMENT '评论时间',
  `uid` int(11) NOT NULL COMMENT '评论所属用户id',
  `hid` int(11) NOT NULL COMMENT '评论所属酒店id',
  `oid` int(11) NOT NULL COMMENT '评论所属订单id',
*/

@Entity
@Table(name = "t_comment")
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cid; //评论id
	private String content; //评论内容
	private Float rate; //评论等级
	private Integer type; //评论类别（0表示差评、1表示中评、2表示好评）
	private Date date; //评论时间
	
	@ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	@JoinColumn(name = "uid")
	private User user; //评论所属用户
	
	@ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	@JoinColumn(name = "hid")
	private Hotel hotel; //评论所属酒店
	
	@OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	@JoinColumn(name = "oid")
	private Order order; //评论所属订单
	
	public Long getCid() {
		return cid;
	}
	public void setCid(Long cid) {
		this.cid = cid;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Float getRate() {
		return rate;
	}
	public void setRate(Float rate) {
		this.rate = rate;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	@Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy/MM/dd HH:mm:ss",timezone="GMT+8")
	@DateTimeFormat(pattern="yy/MM/dd HH:mm:ss")
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Hotel getHotel() {
		return hotel;
	}
	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	@Override
	public String toString() {
		return "Comment [cid=" + cid + ", content=" + content + ", rate=" + rate + ", type=" + type + ", date=" + date
				+ "]";
	}
	
}