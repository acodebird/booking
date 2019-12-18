package com.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.booking.domain.User;
import com.booking.repository.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;

	// 根据用户 id 获取用户信息
	public User getUserById(Long uid){
		return userRepository.findById(uid).get();
	}
	// 增加用户\更新用户
	public User save (User user){
		return userRepository.save(user);
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
	public Page<User> findAll(Specification<User> spec, Pageable pageable){
		return userRepository.findAll(spec, pageable);
	}

	public Page<User> findAll(Pageable pageable){
		return userRepository.findAll(pageable);
	}
	public List<User> findAllById(List<Long> uids){
		return (List<User>) userRepository.findAllById(uids);
	}

	public boolean existsById(Long uid){
		return userRepository.existsById(uid);
	}
	public long count() {
		return userRepository.count();
	}
}
