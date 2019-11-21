package com.booking.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

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
@Table(name = "t_hotel")
public class Hotel {
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
	private String type; //酒店类型
	private Float rate; //酒店评分
	
	//酒店与订单建立双向关联关系，由多的一方订单维护外键
	@OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Order> orders = new HashSet<Order>();
	
	//酒店与评论建立双向关联关系，由多的一方评论维护外键
	@OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Comment> comments = new HashSet<Comment>();
	
	//酒店与房型建立双向关联关系，由多的一方房型维护外键
	@OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Room> rooms = new HashSet<Room>();
	
	public Long getHid() {
		return hid;
	}
	public void setHid(Long hid) {
		this.hid = hid;
	}
	public String getHname() {
		return hname;
	}
	public void setHname(String hname) {
		this.hname = hname;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFacilities() {
		return facilities;
	}
	public void setFacilities(String facilities) {
		this.facilities = facilities;
	}
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Float getRate() {
		return rate;
	}
	public void setRate(Float rate) {
		this.rate = rate;
	}
	public Set<Order> getOrders() {
		return orders;
	}
	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}
	public Set<Comment> getComments() {
		return comments;
	}
	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}
	public Set<Room> getRooms() {
		return rooms;
	}
	public void setRooms(Set<Room> rooms) {
		this.rooms = rooms;
	}
	@Override
	public String toString() {
		return "Hotel [hid=" + hid + ", hname=" + hname + ", address=" + address + ", description=" + description
				+ ", facilities=" + facilities + ", service=" + service + ", phone=" + phone + ", type=" + type
				+ ", rate=" + rate + "]";
	}
	
}
