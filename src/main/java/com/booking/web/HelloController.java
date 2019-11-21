package com.booking.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="hello")
public class HelloController {
	
	@RequestMapping
	public String sayHello() {
		return "hello!";
	}
}
