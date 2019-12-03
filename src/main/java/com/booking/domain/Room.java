package com.booking.domain;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/*`rid` int(11) NOT NULL AUTO_INCREMENT COMMENT '房型id',
  `rname` varchar(255) NOT NULL COMMENT '房型名',
  `type` varchar(255) NOT NULL COMMENT '房型类别',
  `breakfast` varchar(255) NOT NULL COMMENT '房型早餐',
  `cancel` varchar(255) NOT NULL COMMENT '房型取消策略',
  `people` int(11) NOT NULL COMMENT '房型人数上限',
  `price` double NOT NULL COMMENT '房型价格',
  `additions` varchar(255) NOT NULL COMMENT '房型详情',
  `img` varchar(255) NOT NULL COMMENT '房型图片',
  `hid` int(11) NOT NULL COMMENT '房型所属酒店id',
 */

@Entity
@Data
@Table(name = "t_room")
public class Room {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long rid; //房型id
	private String rname; //房型名
	private String type; //房型类别
	private String breakfast; //房型早餐
	private String cancel; //房型取消策略
	private Integer people; //房型人数上限
	private Double price; //房型价格
	private String assitions; //房型详情
	private String img; //房型图片
	
	@ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	@JoinColumn(name = "hid")
	private Hotel hotel; //房型所属酒店
	
	//房型与订单建立双向关联关系，由多的一方订单维护外键
	@OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Order> orders = new HashSet<Order>();

	@Override
	public String toString() {
		return "Room [rid=" + rid + ", rname=" + rname + ", type=" + type + ", breakfast=" + breakfast + ", cancel="
				+ cancel + ", people=" + people + ", price=" + price + ", assitions=" + assitions + ", img=" + img
				+ "]";
	}
	
}
