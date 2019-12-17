package com.booking.web;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.booking.domain.Hotel;
import com.booking.domain.Order;
import com.booking.service.HotelService;
import com.booking.utils.ResponseEntity;
import com.booking.utils.STablePageRequest;

@RestController
@RequestMapping(value="/hotelManage")
public class HotelController {
	@Autowired
	HotelService hotelService;
	
	/**
     * 获取一页酒店
     * @param pageable
     * @return
     */
    @GetMapping
    public ResponseEntity<Page<Order>> getHotelPage(STablePageRequest pageable) {
        Page<Hotel> page = Page.empty(pageable.getPageable());
        page = hotelService.findAll(pageable.getPageable());

        return ResponseEntity.ofSuccess().status(HttpStatus.OK).data(page);
    }
    /**
     * 根据id删除酒店信息
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity deleteHotelById(@PathVariable("id") Long id) {
    	hotelService.deleteById(id);
        return ResponseEntity.ofSuccess().status(HttpStatus.OK);
    }
    /**
     * 批量删除酒店信息
     * @param ids
     * @return
     */
    @DeleteMapping
    public ResponseEntity deleteHotelByIds(Long[] ids) {
    	hotelService.deleteAll(Arrays.asList(ids));
    	return ResponseEntity.ofSuccess().status(HttpStatus.OK);
    }
    /**
     * 添加一条酒店信息
     * @param hotel
     * @return
     */
    @PostMapping
    public ResponseEntity addHotel(@RequestBody Hotel hotel) {
    	System.out.println(hotel.toString());
    	hotelService.save(hotel);
    	return ResponseEntity.ofSuccess().status(HttpStatus.OK);
    }
}
