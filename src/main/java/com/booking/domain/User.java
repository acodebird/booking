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

/* 
 * `uid` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户主键',
  `uname` varchar(255) NOT NULL COMMENT '用户名',
  `upassword` varchar(255) NOT NULL COMMENT '用户密码',
  `salt` varchar(255) NOT NULL COMMENT '用户密码盐',
  `email` varchar(255) NOT NULL COMMENT '用户邮箱',
  `telephone` varchar(255) NOT NULL COMMENT '用户电话',
  `type` int(11) NOT NULL COMMENT '用户类型（0表示普通用户、1表示管理员）',
  `icon` varchar(255) NOT NULL COMMENT '用户头像',
  `enable` bit(1) NOT NULL COMMENT '用户可用性（0表示不可用，1表示可用）'
  */
@Entity
@Table(name = "t_user")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long uid; //用户主键
	private String uname; //用户名
	private String upassword; //用户密码
	private String salt; //用户密码盐
	private String email; //用户邮箱
	@Size(min=11,max=11)
	private String telephone; //用户电话
	private Integer type; //用户类型（0表示普通用户、1表示管理员）
	private String icon; //用户头像
	private Boolean enable; //用户可用性（0表示不可用，1表示可用）
	
	//用户与订单建立双向关联关系，由多的一方订单维护外键
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Order> orders = new HashSet<Order>();
	
	//用户与评论建立双向关联关系，由多的一方评论维护外键
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Comment> comments = new HashSet<Comment>();
	
	public Long getUid() {
		return uid;
	}
	public void setUid(Long uid) {
		this.uid = uid;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getUpassword() {
		return upassword;
	}
	public void setUpassword(String upassword) {
		this.upassword = upassword;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Boolean getEnable() {
		return enable;
	}
	public void setEnable(Boolean enable) {
		this.enable = enable;
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
	@Override
	public String toString() {
		return "User [uid=" + uid + ", uname=" + uname + ", upassword=" + upassword + ", salt=" + salt + ", email="
				+ email + ", telephone=" + telephone + ", type=" + type + ", icon=" + icon + ", enable=" + enable + "]";
	}
	
}
