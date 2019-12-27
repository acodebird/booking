package com.booking.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;

import com.booking.domain.Comment;
import com.booking.enums.CommentTypeEnum;

import lombok.Getter;
import lombok.Setter;

//评论多条件查询DTO
@Getter
@Setter
public class CommentQueryDTO {
	private Long cidKey;
	private Long hidKey;
	private CommentTypeEnum typeKey;
	private String unameKey;
	private String hnameKey;
	private Float minRateKey = 0f;
	private Float maxRateKey = 5f;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateKey;
	
	@SuppressWarnings("serial")
	public static Specification<Comment> getSpecification(final CommentQueryDTO commentQueryDTO){
		return new Specification<Comment>() {

			@Override
			public Predicate toPredicate(Root<Comment> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicate = new ArrayList<Predicate>();
				
				//拼接评论编号，等值查询
				if(null != commentQueryDTO.getCidKey()) {
					predicate.add(criteriaBuilder.equal(root.get("cid").as(Long.class), commentQueryDTO.getCidKey()));
				}
				
				//拼接酒店编号，等值查询
				if(null != commentQueryDTO.getHidKey()) {
					predicate.add(criteriaBuilder.equal(root.get("hotel").get("hid").as(Long.class), commentQueryDTO.getHidKey()));
				}
				
				//拼接评论类型,等值查询
				if(null != commentQueryDTO.getTypeKey()) {
					predicate.add(criteriaBuilder.equal(root.get("type").as(CommentTypeEnum.class), commentQueryDTO.getTypeKey()));
				}
				
				//拼接酒店名字,模糊匹配
				if(StringUtils.isNotBlank(commentQueryDTO.getHnameKey())) {
					predicate.add(criteriaBuilder.like(root.get("hotel").get("hname").as(String.class), "%" + commentQueryDTO.getHnameKey() + "%"));
				}
				
				//拼接用户姓名,模糊匹配
				if(StringUtils.isNotBlank(commentQueryDTO.getUnameKey())) {
					predicate.add(criteriaBuilder.like(root.get("user").get("uname").as(String.class), "%" + commentQueryDTO.getUnameKey() + "%"));
				}
				
				//拼接评分区间,范围查询
				predicate.add(criteriaBuilder.between(root.get("rate").as(Float.class), commentQueryDTO.getMinRateKey(), commentQueryDTO.getMaxRateKey()));
				
				//拼接评论时间,范围查询
                if (null != commentQueryDTO.getDateKey()) {
                    predicate.add(criteriaBuilder.greaterThanOrEqualTo(root.get("date").as(Date.class), commentQueryDTO.getDateKey()));
                }
				
				Predicate[] predicates = new Predicate[predicate.size()];
				return query.where(predicate.toArray(predicates)).getRestriction();
			}
			
		};
	}
}
