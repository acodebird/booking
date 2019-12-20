package com.booking.service;

import com.booking.domain.User;

public interface UserService {
	void save(User user);
	User findById(Long id);
}
