package com.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.booking.domain.User;
import com.booking.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;

	@Override
	public void save(User user) {
		userRepository.save(user);
	}

	@Override
	public User findById(Long id) {
		return userRepository.findById(id).get();
	}

}
