package com.booking.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.booking.enums.HotelTypeEnum;

import lombok.Getter;
import lombok.Setter;

/*`hid` int(11) NOT NULL AUTO_INCREMENT COMMENT '酒店主键',
  `hname` varchar(255) NOT NULL COMMENT '酒店名称',
  `address` varchar(255) NOT NULL COMMENT '酒店地址',
  `description` varchar(255) NOT NULL COMMENT '酒店信息',
  `facilities` varchar(255) NOT NULL COMMENT '酒店设施',
  `service` varchar(255) NOT NULL COMMENT '酒店服务',
  `phone` varchar(255) NOT NULL COMMENT '酒店电话',
  `type` varchar(255) NOT NULL COMMENT '酒店类型',
  `rate` float NOT NULL COMMENT '酒店评分',
  `img` varchar(255) NOT NULL COMMENT '酒店图片',
  */

@Entity
//@Data(@Data可能会出现toString()/hashCode()/equals()死循环，具体解析请看https://www.jianshu.com/p/61d4e28ee254)
@Getter
@Setter
@Table(name = "t_hotel")
public class Hotel implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long hid; //酒店主键
	private String hname; //酒店名称
	private String address; //酒店地址
	private String description; //酒店信息
	private String facilities; //酒店设施
	private String service; //酒店服务
	@Size(min=11,max=11)
	private String phone; //酒店电话
	private HotelTypeEnum type; //酒店类型(APARTMENT：公寓, HOMESTAY：民宿, HOSTEL：青旅, ECONOMY：经济连锁, HIGNEND：高级连锁)
	private Float rate; //酒店评分
	private String img; //酒店图片
	
//	//酒店与订单建立双向关联关系，由多的一方订单维护外键
//	@OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//	@JsonBackReference
//	private Set<Order> orders = new HashSet<Order>();
//
//	//酒店与评论建立双向关联关系，由多的一方评论维护外键
//	@OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//	@JsonBackReference
//	private Set<Comment> comments = new HashSet<Comment>();
//
	//酒店与房型建立双向关联关系，由多的一方房型维护外键
//	@OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//	@JsonBackReference
//	private Set<Room> rooms = new HashSet<Room>();

	@Override
	public String toString() {
		return "Hotel [hid=" + hid + ", hname=" + hname + ", address=" + address + ", description=" + description
				+ ", facilities=" + facilities + ", service=" + service + ", phone=" + phone + ", type=" + type
				+ ", rate=" + rate + "]";
	}
	
}
