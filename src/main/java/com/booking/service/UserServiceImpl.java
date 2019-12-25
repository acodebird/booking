package com.booking.service;

import com.booking.domain.Comment;
import com.booking.domain.Order;
import com.booking.dto.UserQueryDTO;
import com.booking.repository.CommentRepository;
import com.booking.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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

	public static final String EHCACHE_NAME = "user";

	// 根据用户 id 获取用户信息
	@Transactional(readOnly=true)
	@Cacheable(value=EHCACHE_NAME,key="'user_'+#uid")
	public User getUserById(Long uid){
		return userRepository.findById(uid).get();
	}
	// 根据用户 email 获取用户信息
	@Transactional(readOnly=true)
	@Cacheable(value=EHCACHE_NAME,key="'user_'+#email")
	public User findByEmail(String email){
		UserQueryDTO dto = new UserQueryDTO();
		dto.setEmail(email);
		List<User>users=userRepository.findAll(UserQueryDTO.getWhereClause(dto));
		return users.size()<1?null:users.get(0);
	}
	// 增加用户\更新用户
	@CachePut(value=EHCACHE_NAME,key="'user_'+#user.getUid()")
	public User save (User user){
		return userRepository.save(user);
	}
	public void saveAll (List<User> users){
		userRepository.saveAll(users);
	}
	// 删除用户
	@CacheEvict(value=EHCACHE_NAME,key="'user_'+#uid")
	public void deleteById(Long uid){
		UserQueryDTO dto = new UserQueryDTO();
		List<Long> uids = dto.getUids();
		uids.add(uid);
		dto.setUids(uids);
		deleteDependence(dto);
		userRepository.deleteById(uid);
	}
//	public void delete(User user){
//		userRepository.delete(user);
//	}
	// 批量删除用户
//	public void deleteAll(List<User> users){
//		userRepository.deleteAll(users);
//	}
//	public void deleteAll(Long[] uids){
//		List<Long> uidLists = new ArrayList<Long>(Arrays.asList(uids));
//		List<User> users = (List<User>) userRepository.findAllById(uidLists);
//		userRepository.deleteAll(users);
//	}
	public void deleteAll(List<Long> uids){
		UserQueryDTO dto = new UserQueryDTO();
		dto.setUids(uids);
		deleteDependence(dto);
		List<User> users = (List<User>) userRepository.findAllById(uids);
		userRepository.deleteAll(users);
	}
	public void deleteDependence(UserQueryDTO dto){
		List<Order> orders = orderRepository.findAll(UserQueryDTO.getOrderSepcByUser(dto));
		List<Comment> comments = commentRepository.findAll(UserQueryDTO.getCommentSepcByUser(dto));
		commentRepository.deleteAll(comments);
		orderRepository.deleteAll(orders);
	}

	// 获取用户列表
	@Transactional(readOnly=true)
	public Page<User> findAll(Specification<User> spec, Pageable pageable){
		return userRepository.findAll(spec, pageable);
	}
//	@Transactional(readOnly=true)
//	public Page<User> findAll(Pageable pageable){
//		return userRepository.findAll(pageable);
//	}
//	@Transactional(readOnly=true)
//	public List<User> findAll(){
//		return (List<User>) userRepository.findAll();
//	}
	@Transactional(readOnly=true)
	public List<User> findAll(List<Long> uids){
		return (List<User>) userRepository.findAllById(uids);
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
	public boolean existsById(Long uid){
		return userRepository.existsById(uid);
	}
	@Transactional(readOnly=true)
	public boolean existsByEmail(String email){
		UserQueryDTO dto=new UserQueryDTO();
		dto.setEmail(email);
		return userRepository.findAll(UserQueryDTO.getWhereClause(dto)).size()>0?true:false;
	}
	@Transactional(readOnly=true)
	public long count() {
		return userRepository.count();
	}
}
