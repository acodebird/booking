package com.booking.service;

import com.booking.domain.Hotel;
import com.booking.domain.Room;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.booking.domain.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest
public class TestService {
	@Autowired
	private UserService userService;

	@Autowired
    private HotelService hotelService;

	@Autowired
	private RoomService roomService;
	
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

	/**
	 * 酒店数据注入
	 */
	@Test
    public void testSaveHotel() {
		for (int i = 0; i < 20; i++) {
			Hotel hotel = new Hotel();
			hotel.setHname("维也纳国际酒店" + i);
			hotel.setAddress("广东省东莞市大学路" + i + "号");
			hotel.setDescription("于2010年开业，东莞市维也纳国际酒店旗舰店，适合广大人群入驻");
			hotel.setFacilities("停车场,餐厅");
			hotel.setService("接待外宾，叫醒服务");
			hotel.setPhone("12345678910");
			hotel.setType("高档型");
			hotel.setRate(4.8f);
			hotel.setOrders(null);
			hotel.setComments(null);
//			hotel.setRooms(null);
			hotelService.save(hotel);
		}
    }

	/**
	 * 房型数据注入
	 */
	@Test
	public void testSaveRoom() {
		Iterable<Hotel> allHotel = hotelService.findAll();
		allHotel.forEach(hotel -> {
			for (int i = 0; i < 4; i++) {
				Room room = new Room();
				if (i % 2 == 0) {
					room.setRname("精品单床房" + i);
					room.setType("单人房");
					room.setBreakfast("含早");
					room.setCancel("免费取消");
					room.setPeople(2);
					room.setPrice(199.9);
					room.setAssitions("床型：单床1.8米,面积：12㎡,不允许加床,外窗");
				} else {
					room.setRname("豪华双床房" + i);
					room.setType("双人房");
					room.setBreakfast("不含早");
					room.setCancel("限时取消");
					room.setPeople(4);
					room.setPrice(249.9);
					room.setAssitions("床型：双床1.8米,面积：30㎡,不允许加床,外窗");
				}
				room.setImg("xxxxxxxxxx");
				room.setOrders(null);
//				room.setHotel(hotelService.findById(hotel.getHid()));
				room.setHotel(hotel);
				roomService.save(room);
			}
		});
	}

}
