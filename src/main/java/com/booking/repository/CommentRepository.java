package com.booking.repository;

import com.booking.domain.Comment;
import com.booking.domain.User;
import com.booking.enums.CommentTypeEnum;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaSpecificationExecutor<Comment>, PagingAndSortingRepository<Comment, Long> {
    @Query(value = "select * from t_comment where oid = ?1", nativeQuery = true)
    Comment findAllByOid(Long oid);
    @Query(value = "select * from t_comment where hid = ?1", nativeQuery = true)
    List<Comment> findAllByHid(Long hid);
    @Query(value = "select count(*) from t_comment where hid = ?1 and type = ?2", nativeQuery = true)
    Long countType(Long hid, Integer type);
}
