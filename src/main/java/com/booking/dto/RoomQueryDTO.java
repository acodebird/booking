package com.booking.dto;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.booking.domain.Room;
import com.booking.enums.RoomTypeEnum;

import lombok.Getter;
import lombok.Setter;

//房间多条件查询DTO
@Getter
@Setter
public class RoomQueryDTO {
	private Long ridKey;
	private Long hid;
	private String rnameKey;
	private RoomTypeEnum typeKey;
	private Double minPrice = 0d;
	private Double maxPrice = 9000d;
	
	@SuppressWarnings("serial")
	public static Specification<Room> getSpecification(final RoomQueryDTO roomQueryDTO){
		return new Specification<Room>() {

			@Override
			public Predicate toPredicate(Root<Room> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicate = new ArrayList<Predicate>();
				
				//拼接房间编号，等值查询
				if(null != roomQueryDTO.getRidKey()) {
					predicate.add(criteriaBuilder.equal(root.get("rid").as(Long.class), roomQueryDTO.getRidKey()));
				}
				
				//拼接酒店编号，等值查询
				if(null != roomQueryDTO.getHid()) {
					predicate.add(criteriaBuilder.equal(root.get("hotel").get("hid").as(Long.class), roomQueryDTO.getHid()));
				}
				
				//拼接房间名字,模糊匹配
				if(StringUtils.isNotBlank(roomQueryDTO.getRnameKey())) {
					predicate.add(criteriaBuilder.like(root.get("rname").as(String.class), "%" + roomQueryDTO.getRnameKey() + "%"));
				}
				
				//拼接酒店类型,等值查询
				if(null != roomQueryDTO.getTypeKey()) {
					predicate.add(criteriaBuilder.equal(root.get("type").as(RoomTypeEnum.class), roomQueryDTO.getTypeKey()));
				}
				
				//拼接评分区间,范围查询
				predicate.add(criteriaBuilder.between(root.get("price").as(Double.class), roomQueryDTO.getMinPrice(), roomQueryDTO.getMaxPrice()));
				
				Predicate[] predicates = new Predicate[predicate.size()];
				return query.where(predicate.toArray(predicates)).getRestriction();
			}
			
		};
	}
}
