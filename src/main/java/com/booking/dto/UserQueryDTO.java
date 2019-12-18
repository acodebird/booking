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
import java.util.List;

@Getter
@Setter
public class UserQueryDTO {
    private String uname;
    private Integer type ;
    private Boolean enable;

    // 2.根据查询条件是否有值 - 动态拼接动态查询条件对象 Specification<T> spec
    @SuppressWarnings("serial")
    public static Specification<User> getWhereClause(final UserQueryDTO userQueryDTO) {
        return new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

                List<Predicate> predicate = new ArrayList<>();
                if (StringUtils.isNotBlank(userQueryDTO.getUname())) {
                    predicate.add(cb.like(root.get("uname").as(String.class),
                            "%" + userQueryDTO.getUname() + "%"));
                }
                if (null != userQueryDTO.getType()) {
                    predicate.add(cb.equal(root.get("type").as(Integer.class),
                            userQueryDTO.getType()));
                }
                if (null != userQueryDTO.getEnable()) {
                    predicate.add(cb.equal(root.get("enable").as(Boolean.class),
                            userQueryDTO.getEnable()));
                }

                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();// and
            }
        };
    }

}
