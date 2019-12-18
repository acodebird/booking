package com.booking.service;

import com.booking.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {
	// 根据用户 id 获取用户信息
	@Transactional(readOnly=true)
	public User getUserById(Long uid);
	// 增加用户\更新用户
	public User save (User user);
	// 删除用户
	public void deleteById(Long uid);
	public void delete(User user);
	// 批量删除用户
	public void deleteAll(List<User> users);
	public void deleteAll(Long[] uids);
	public void deleteAllById(List<Long> uids);
	// 获取用户列表
	@Transactional(readOnly=true)
	public Page<User> findAll(Specification<User> spec, Pageable pageable);
	@Transactional(readOnly=true)
	public Page<User> findAll(Pageable pageable);
	@Transactional(readOnly=true)
	public List<User> findAllById(List<Long> uids);
	@Transactional(readOnly=true)
	public boolean existsById(Long uid);
	@Transactional(readOnly=true)
	public long count();
}
