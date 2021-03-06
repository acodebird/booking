package com.booking.service;

import com.booking.domain.Comment;
import com.booking.domain.Order;
import com.booking.domain.User;
import com.booking.dto.UserQueryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface UserService {
	// 根据用户 id 获取用户信息
	public User getUserById(Long uid);
	public User findByEmail(String email);
	// 增加用户\更新用户
	public User save (User user);
	public void saveAll (List<User> users);
	// 删除用户
	public void deleteById(Long uid);
	//public void delete(User user);
	// 批量删除用户
	//public void deleteAll(List<User> users);
	//public void deleteAll(Long[] uids);
	public void deleteAll(List<Long> uids);
	public void deleteDependence(UserQueryDTO dto);
	// 获取用户列表
	public Page<User> findAll(Specification<User> spec, Pageable pageable);
	//public Page<User> findAll(Pageable pageable);
	//public List<User> findAll();
	public List<User> findAll(List<Long> uids);

	// 获取用户订单
	public List<Order> findAllOrder(Specification<Order> spec);
	public Page<Order> findAllOrder(Specification<Order> spec, Pageable pageable);
	//获取用户评论
	public List<Comment> findAllComment(Specification<Comment> spec);
	public Page<Comment> findAllComment(Specification<Comment> spec, Pageable pageable);

	public boolean existsById(Long uid);
	public boolean existsByEmail(String email);
	public long count();

//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
