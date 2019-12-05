package com.booking.service;

import com.booking.domain.Hotel;
import com.booking.domain.Room;
import com.booking.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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
			hotel.setRooms(null);
			hotelService.save(hotel);
		}
    }
	
	/**
	 * 测试删除酒店级联删除房型
	 */
	@Test
	public void testDeleteHotel() {
		hotelService.deleteById(3L);
	}
	/*
	 * 测试根据id查询酒店以及更新酒店
	 * 
	 */
	@Test
	public void testFindByIdAndUpdateHotel() {
		Hotel hotel = hotelService.findById(1L);
		System.out.println(hotel);
		hotel.setAddress("广东省东莞市大学路test0号");
		hotel.setPhone("13113112115");
		System.out.println(hotel);
		hotelService.save(hotel);
	}
	
	/**
	 * 测试删除房型
	 */
	@Test
	public void testDeleteRoom() {
		roomService.deleteById(3L);
	}
	
	/**
	 * 测试根据id查询房型以及更新房型
	 */
	@Test
	public void testFindByIdAndUpdateRoom() {
		Room room = roomService.findById(1L);
		System.out.println(room);
		room.setPeople(6);
		room.setType("亲子房");
		System.out.println(room);
		roomService.save(room);
	}
	/**
	 * 测试查找所有房型
	 */
	@Test
	public void testFindAllRoom() {
		Iterable<Room> rooms = roomService.findAll();
		rooms.forEach(room -> {
			System.out.println(room);
		});
	}
	
	/**
	 * 房型数据注入
	 */
	@Test
	public void testSaveRoom() {
		List<Hotel> allHotel = hotelService.findAll();
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
				room.setHotel(hotel);
				roomService.save(room);
			}
		});
	}

}
