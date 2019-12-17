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

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.Setter;

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
//@Data(@Data可能会出现toString()/hashCode()/equals()死循环，具体解析请看https://www.jianshu.com/p/61d4e28ee254)
@Setter
@Getter
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
	@JsonBackReference
	private Set<Order> orders = new HashSet<Order>();
	
	//用户与评论建立双向关联关系，由多的一方评论维护外键
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonBackReference
	private Set<Comment> comments = new HashSet<Comment>();

	@Override
	public String toString() {
		return "User [uid=" + uid + ", uname=" + uname + ", upassword=" + upassword + ", salt=" + salt + ", email="
				+ email + ", telephone=" + telephone + ", type=" + type + ", icon=" + icon + ", enable=" + enable + "]";
	}
	
}
