package com.booking.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.booking.domain.Comment;
import com.booking.enums.CommentTypeEnum;

public interface CommentService {
	/**
     * 批量删除评论
	 */
    void deleteAll(List<Long> ids);
	/**
     * 根据id删除评论
     */
    void deleteById(Long cid);
	/**
	 * 条件获取一页评论
	 * @param specification
	 * @param pageable
	 * @return
	 */
	Page<Comment> findAll(Specification<Comment> specification, Pageable pageable);
	/**
	 * 根据订单id获取评论
	 * @param oid
	 * @return
	 */
    Comment findAllByOid(Long oid);
    /**
     * 保存/更新一条评论
     * @param comment
     * @return
     */
    Comment save(Comment comment);
    /**
     * 批量保存评论
     * @param comments
     */
    void save(List<Comment> comments);
    /**
     * 删除评论
     * @param comments
     */
    void deleteByEntity(Comment comment);
    /**
     * 根据评论id获取评论详情
     * @param cid
     * @return
     */
	Comment findById(Long cid);
	/**
	 * 根据酒店id获取所有评论
	 * @param hid
	 * @return
	 */
	List<Comment> findByHid(Long hid);
	/**
	 * 根据酒店id统计评论类型数量
	 * @param hid
	 * @param commentTypeEnum
	 * @return
	 */
	Long countType(Long hid, Integer type);
}
