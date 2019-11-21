package com.booking.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.booking.domain.User;

@SpringBootTest
public class TestService {
	@Autowired
	private UserService userService;
	
	@Test
	public void test1() {
		User user = new User();
		user.setUname("老王");
		user.setSalt("aaa");
		user.setUpassword("12345");
		user.setEmail("acodebird@163.com");
		user.setEnable(true);
		user.setIcon("xxxx");
		user.setType(1);
		user.setTelephone("13112124724");
		user.setComments(null);
		user.setOrders(null);
		userService.save(user);
	}
	
	@Test
	public void test2() {
		User user = userService.getUserById(1L);
		System.out.println(user.toString());
	}
}
