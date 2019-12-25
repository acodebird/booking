package com.booking.dto;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.booking.domain.Hotel;
import com.booking.enums.HotelTypeEnum;

import lombok.Getter;
import lombok.Setter;


//酒店多条件查询DTO
@Getter
@Setter
public class HotelQueryDTO {
	private Long hidKey;
	private String hnameKey;
	private String addressKey;
	private HotelTypeEnum typeKey;
	private Float minRateKey = 0f;
	private Float maxRateKey = 5f;
	
	@SuppressWarnings("serial")
	public static Specification<Hotel> getSpecification(final HotelQueryDTO hotelQueryDTO){
		return new Specification<Hotel>() {

			@Override
			public Predicate toPredicate(Root<Hotel> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicate = new ArrayList<Predicate>();
				
				//拼接酒店编号，等值查询
				if(null != hotelQueryDTO.getHidKey()) {
					predicate.add(criteriaBuilder.equal(root.get("hid").as(Long.class), hotelQueryDTO.getHidKey()));
				}
				
				//拼接酒店名字,模糊匹配
				if(StringUtils.isNotBlank(hotelQueryDTO.getHnameKey())) {
					predicate.add(criteriaBuilder.like(root.get("hname").as(String.class), "%" + hotelQueryDTO.getHnameKey() + "%"));
				}
				
				//拼接酒店地址,模糊匹配
				if(StringUtils.isNotBlank(hotelQueryDTO.getHnameKey())) {
					predicate.add(criteriaBuilder.like(root.get("address").as(String.class), "%" + hotelQueryDTO.getAddressKey() + "%"));
				}
				
				//拼接酒店类型,等值查询
				if(null != hotelQueryDTO.getTypeKey()) {
					predicate.add(criteriaBuilder.equal(root.get("type").as(HotelTypeEnum.class), hotelQueryDTO.getTypeKey()));
				}
				
				//拼接评分区间,范围查询
				predicate.add(criteriaBuilder.between(root.get("rate").as(Float.class), hotelQueryDTO.getMinRateKey(), hotelQueryDTO.getMaxRateKey()));
				
				Predicate[] predicates = new Predicate[predicate.size()];
				return query.where(predicate.toArray(predicates)).getRestriction();
			}
			
		};
	}
}
