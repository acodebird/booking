package com.booking.service;

import com.booking.domain.Comment;
import com.booking.domain.Order;
import com.booking.repository.CommentRepository;
import com.booking.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.booking.domain.User;
import com.booking.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private CommentRepository commentRepository;

	// 根据用户 id 获取用户信息
	@Transactional(readOnly=true)
	public User findById(Long uid){
		return userRepository.findById(uid).get();
	}
	// 增加用户\更新用户
	public User save (User user){
		return userRepository.save(user);
	}
	public void saveAll (List<User> users){
		userRepository.saveAll(users);
	}
	// 删除用户
	public void deleteById(Long uid){
		userRepository.deleteById(uid);
	}
	public void delete(User user){
		userRepository.delete(user);
	}
	// 批量删除用户
	public void deleteAll(List<User> users){
		userRepository.deleteAll(users);
	}
	public void deleteAll(Long[] uids){
		List<Long> uidLists = new ArrayList<Long>(Arrays.asList(uids));

		List<User> users = (List<User>) userRepository.findAllById(uidLists);
		userRepository.deleteAll(users);
	}
	public void deleteAllById(List<Long> uids){
		List<User> users = (List<User>) userRepository.findAllById(uids);
		userRepository.deleteAll(users);
	}
	// 获取用户列表
	@Transactional(readOnly=true)
	public Page<User> findAll(Specification<User> spec, Pageable pageable){
		return userRepository.findAll(spec, pageable);
	}
	// 获取用户订单
	@Transactional(readOnly=true)
	public List<Order> findAllOrder(Specification<Order> spec){
		return orderRepository.findAll(spec);
	}
	@Transactional(readOnly=true)
	public Page<Order> findAllOrder(Specification<Order> spec, Pageable pageable){
		return orderRepository.findAll(spec,pageable);
	}
	//获取用户评论
	public List<Comment> findAllComment(Specification<Comment> spec){
		return commentRepository.findAll(spec);
	}
	public Page<Comment> findAllComment(Specification<Comment> spec, Pageable pageable){
		return commentRepository.findAll(spec,pageable);
	}

	@Transactional(readOnly=true)
	public Page<User> findAll(Pageable pageable){
		return userRepository.findAll(pageable);
	}
	@Transactional(readOnly=true)
	public List<User> findAllById(List<Long> uids){
		return (List<User>) userRepository.findAllById(uids);
	}

	@Transactional(readOnly=true)
	public boolean existsById(Long uid){
		return userRepository.existsById(uid);
	}
	@Transactional(readOnly=true)
	public long count() {
		return userRepository.count();
	}
}
