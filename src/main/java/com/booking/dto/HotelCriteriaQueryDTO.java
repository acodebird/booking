package com.booking.dto;

import com.booking.domain.Hotel;
import com.booking.enums.HotelTypeEnum;
import com.booking.enums.LocationEnum;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Data
public class HotelCriteriaQueryDTO {
    private LocationEnum location;
    private List<String> brands;
    private List<HotelTypeEnum> types;
    private Double minPrice = 0.0;
    private Double maxPrice = 9999.0;
    private String address;
    private String hname;

    private int pageNo = 1;
    private int pageSize = 10;
    //排序条件
    private String sortField = "rate";
    private String sortOrder = "descend";

    @SuppressWarnings("serial")
    public static Specification<Hotel> getSpecification(final HotelCriteriaQueryDTO dto) {
        return new Specification<Hotel>() {
            @Override
            public Predicate toPredicate(Root<Hotel> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicate = new ArrayList<Predicate>();

                //拼接位置
                if (null != dto.getLocation()) {
                    predicate.add(criteriaBuilder.equal(root.get("location").as(LocationEnum.class), dto.getLocation()));
                }

                //拼接品牌
                if (dto.getBrands().size() != 0) {
                    Predicate[] temp = new Predicate[dto.getBrands().size()];
                    int i = 0;
                    for (String brand : dto.getBrands()) {
                        temp[i++] = criteriaBuilder.equal(root.get("brand").as(String.class), brand);
                    }
                    predicate.add(criteriaBuilder.or(temp));
                }

                //拼接酒店类型
                if (dto.getTypes().size() != 0) {
                    Predicate[] temp = new Predicate[dto.getTypes().size()];
                    int i = 0;
                    for (HotelTypeEnum type : dto.getTypes()) {
                        temp[i++] = criteriaBuilder.equal(root.get("type").as(HotelTypeEnum.class), type);
                    }
                    predicate.add(criteriaBuilder.or(temp));
                }

                //拼接价格区间
                predicate.add(criteriaBuilder.between(root.get("landprice").as(Double.class), dto.getMinPrice(), dto.getMaxPrice()));

                //拼接地址
                if (StringUtils.isNotBlank(dto.getAddress())) {
                    predicate.add(criteriaBuilder.like(root.get("address").as(String.class), "%" + dto.getAddress() + "%"));
                }

                //拼接酒店名称
                if (StringUtils.isNotBlank(dto.getHname())) {
                    predicate.add(criteriaBuilder.like(root.get("hname").as(String.class), "%" + dto.getHname() + "%"));
                }

                Predicate[] predicates = new Predicate[predicate.size()];
                return query.where(predicate.toArray(predicates)).getRestriction();
            }
        };
    }

    public Pageable getPageable() {
        //前端分页 默认第一页为 1  ， Spring data jpa Pageable 默认第一页为0
        Pageable pageable = null;

        //如果排序条件不为null 或 ""
        if (StringUtils.isNotBlank(sortField) || StringUtils.isNotBlank(sortOrder)) {
            //new 一个默认 降序 排序对象Sort
            Sort pageSort = new Sort(Sort.Direction.DESC, sortField);
            //否则 new 升序  排序对象Sort
            if (!sortOrder.equals("descend")) {
                pageSort = new Sort(Sort.Direction.ASC, sortField);
            }

            //如果排序条件 不为null 或 ""  分页 + 排序
            pageable = PageRequest.of(pageNo - 1, pageSize, pageSort);

        } else {
            //如果排序条件 为null 或 "" 则 只分页 不排序
            pageable = PageRequest.of(pageNo - 1, pageSize);
        }

        return pageable;
    }
}
