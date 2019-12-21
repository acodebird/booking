package com.booking.dto;

import com.booking.domain.Comment;
import com.booking.domain.Order;
import com.booking.domain.User;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserQueryDTO {
    private List<Long> uids=new ArrayList<Long>();
    private String uname;
    private String email;
    private Integer type ;
    private Boolean enable;

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
                if (null != userQueryDTO.getEmail()) {
                    predicate.add(cb.equal(root.get("email").as(String.class),
                            userQueryDTO.getEmail()));
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

    @SuppressWarnings("serial")
    public static Specification<Order> getOrderSepcByUser(final UserQueryDTO userQueryDTO) {
         return new Specification<Order>() {
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<>();
                List<Long> uids=userQueryDTO.getUids();
                if(!uids.isEmpty()){
                    Join<Order,User> join=root.join("user", JoinType.LEFT);
                    CriteriaBuilder.In<Long> in=cb.in(join.get("uid").as(Long.class));
                    for(Long uid:uids){
                        in.value(uid);
                    }
                    predicate.add(in);
                    //predicate.add(cb.equal(join.get("uid").as(Long.class),uid));
                }
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();// and
            }
        };
    }

    @SuppressWarnings("serial")
    public static Specification<Comment> getCommentSepcByUser(final UserQueryDTO userQueryDTO) {
        return new Specification<Comment>() {
            public Predicate toPredicate(Root<Comment> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<>();
                List<Long> uids=userQueryDTO.getUids();
                if(!uids.isEmpty()){
                    Join<Comment,User> join=root.join("user", JoinType.LEFT);
                    CriteriaBuilder.In<Long> in=cb.in(join.get("uid").as(Long.class));
                    for(Long uid:uids){
                        in.value(uid);
                    }
                    predicate.add(in);
                    //predicate.add(cb.equal(join.get("uid").as(Long.class),uid));
                }
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();// and
            }
        };
    }
}
