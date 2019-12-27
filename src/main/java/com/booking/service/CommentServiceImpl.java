package com.booking.service;

import com.booking.domain.Comment;
import com.booking.domain.Hotel;
import com.booking.enums.CommentTypeEnum;
import com.booking.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.transaction.Transactional;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;
    
    /**
	 * 根据订单id获取评论
	 * @param oid
	 * @return
	 */
    @Override
    public Comment findAllByOid(Long oid) {
        return commentRepository.findAllByOid(oid);
    }
    /**
     * 保存/更新一条评论
     * @param comment
     * @return
     */
    @Override
	@Transactional
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }
    /**
     * 批量保存评论
     * @param comments
     */
    @Override
	@Transactional
    public void save(List<Comment> comments) {
        commentRepository.saveAll(comments);
    }
    /**
     * 删除评论
     * @param comments
     */
    @Override
	@Transactional
    public void deleteByEntity(Comment comments) {
        commentRepository.delete(comments);
    }
    /**
     * 根据id删除评论
     */
	@Override
	@Transactional
	public void deleteById(Long cid) {
		commentRepository.deleteById(cid);
	}
	/**
	 * 条件获取一页评论
	 * @param specification
	 * @param pageable
	 * @return
	 */
	@Override
	public Page<Comment> findAll(Specification<Comment> specification, Pageable pageable) {
		return commentRepository.findAll(specification, pageable);
	}
	/**
     * 批量删除评论
	 */
	@Override
	@Transactional
	public void deleteAll(List<Long> ids) {
		Iterable<Comment> hotels = commentRepository.findAllById(ids);
		if(hotels!=null) {
			commentRepository.deleteAll(hotels);
		}
	}
	/**
     * 根据评论id获取订单详情
     * @param cid
     * @return
     */
	@Override
	public Comment findById(Long cid) {
		return commentRepository.findById(cid).get();
	}
	/**
	 * 根据酒店id获取所有评论
	 * @param hid
	 * @return
	 */
	@Override
	public List<Comment> findByHid(Long hid) {
		return commentRepository.findAllByHid(hid);
	}
	/**
	 * 根据酒店id统计评论类型数量
	 * @param hid
	 * @param commentTypeEnum
	 * @return
	 */
	@Override
	public Long countType(Long hid, Integer type) {
		return commentRepository.countType(hid, type);
	}
}
