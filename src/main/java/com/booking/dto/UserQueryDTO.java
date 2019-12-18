package com.booking.dto;

import com.booking.domain.User;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class UserQueryDTO {
    private String UserNumber;
    private Date createTimeStart;
    private Date createTimeEnd;

    // 2.根据查询条件是否有值 - 动态拼接动态查询条件对象 Specification<T> spec
    @SuppressWarnings("serial")
    public static Specification<User> getWhereClause(final UserQueryDTO userQueryDTO) {
        return new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

                List<Predicate> predicate = new ArrayList<>();
                if (StringUtils.isNotBlank(userQueryDTO.getUserNumber())) {
                    predicate.add(cb.like(root.get("UserNumber").as(String.class),
                            "%" + userQueryDTO.getUserNumber() + "%"));
                }
                if (null != userQueryDTO.getCreateTimeStart()) {
                    predicate.add(cb.greaterThanOrEqualTo(root.get("createTime").as(Date.class),
                            userQueryDTO.getCreateTimeStart()));
                }
                if (null != userQueryDTO.getCreateTimeEnd()) {
                    predicate.add(cb.lessThanOrEqualTo(root.get("createTime").as(Date.class),
                            userQueryDTO.getCreateTimeEnd()));
                }

                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();// and
            }
        };
    }

}
