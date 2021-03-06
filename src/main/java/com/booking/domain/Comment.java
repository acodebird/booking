package com.booking.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
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

import com.booking.enums.CommentTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

/*  `cid` int(11) NOT NULL AUTO_INCREMENT COMMENT '评论id',
  `content` varchar(255) NOT NULL COMMENT '评论内容',
  `rate` float NOT NULL COMMENT '评论等级',
  `type` int(11) NOT NULL COMMENT '评论类别（0表示好评、1表示中评、2表示差评）',
  `date` datetime NOT NULL COMMENT '评论时间',
  `uid` int(11) NOT NULL COMMENT '评论所属用户id',
  `hid` int(11) NOT NULL COMMENT '评论所属酒店id',
  `oid` int(11) NOT NULL COMMENT '评论所属订单id',
  `reply` varchar(255) NOT NULL REPLY '评论回复',
*/

@Entity
//@Data(@Data可能会出现toString()/hashCode()/equals()死循环，具体解析请看https://www.jianshu.com/p/61d4e28ee254)
@Setter
@Getter
@Table(name = "t_comment")
public class Comment implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cid; //评论id
	private String content; //评论内容
	private Float rate; //评论等级
	private CommentTypeEnum type; //评论类别（0表示差评、1表示中评、2表示好评）
	private String reply; //店长回复

	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern="yyyy/MM/dd HH:mm:ss",timezone="GMT+8")
	@DateTimeFormat(pattern="yy/MM/dd HH:mm:ss")
	private Date date; //评论时间
	
	@ManyToOne
	@JoinColumn(name = "uid")
	private User user; //评论所属用户
	
	@ManyToOne
	@JoinColumn(name = "hid")
	private Hotel hotel; //评论所属酒店
	
	@OneToOne
	@JoinColumn(name = "oid")
	private Order order; //评论所属订单

	@Override
	public String toString() {
		return "Comment [cid=" + cid + ", content=" + content + ", rate=" + rate + ", type=" + type + ", date=" + date
				+ "]";
	}
	
}
