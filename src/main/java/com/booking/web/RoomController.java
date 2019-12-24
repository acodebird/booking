package com.booking.web;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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
import org.springframework.web.bind.annotation.RestController;

import com.booking.domain.Hotel;
import com.booking.domain.Room;
import com.booking.dto.RoomQueryDTO;
import com.booking.service.HotelService;
import com.booking.service.RoomService;
import com.booking.utils.CopyPropertiesUtil;
import com.booking.utils.ResponseEntity;
import com.booking.utils.STablePageRequest;

@RestController
@RequestMapping(value = "/roomManage")
public class RoomController {
	@Autowired
	private RoomService roomService;
	
	@Autowired
	private HotelService hotelService;
	
	/**
     * 获取一页酒店
     * @param pageable
     * @return
     */
    @GetMapping
    public ResponseEntity<Page<Room>> getRoomPage(STablePageRequest pageable, RoomQueryDTO roomQueryDTO) {
        if (StringUtils.isBlank(pageable.getSortField())) {
            pageable.setSortField("rid");
        }
        Page<Room> page = Page.empty(pageable.getPageable());
        page = roomService.findByHid(RoomQueryDTO.getSpecification(roomQueryDTO), pageable.getPageable());

        return ResponseEntity.ofSuccess().status(HttpStatus.OK).data(page);
    }
    /**
     * 根据id删除房间信息
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity deleteRoomById(@PathVariable("id") Long id) {
    	roomService.deleteById(id);
        return ResponseEntity.ofSuccess().status(HttpStatus.OK);
    }
    /**
     * 批量删除房间信息
     * @param ids
     * @return
     */
    @DeleteMapping
    public ResponseEntity deleteRoomByIds(Long[] ids) {
    	roomService.deleteAll(Arrays.asList(ids));
    	return ResponseEntity.ofSuccess().status(HttpStatus.OK);
    }
    /**
     * 更新房间信息
     * @param hid
     * @param img
     * @return
     */
    @PutMapping("/{rid}")
    public ResponseEntity updateRoomImg(@PathVariable("rid") Long rid,@RequestBody Room room) {
    	Room target = roomService.findById(rid);
    	BeanUtils.copyProperties(room,target,CopyPropertiesUtil.getNullPropertyNames(room));
    	roomService.save(target);
    	return ResponseEntity.ofSuccess().status(HttpStatus.OK);
    }
    /**
     * 添加一条房间信息
     * @param hotel
     * @return
     */
    @PostMapping("/{hid}")
    public ResponseEntity addRoom(@RequestBody Room room, @PathVariable("hid") Long hid) {
    	System.out.println(hid);
    	Hotel hotel = hotelService.findById(hid);
    	room.setHotel(hotel);
    	roomService.save(room);
    	return ResponseEntity.ofSuccess().status(HttpStatus.OK);
    }
}
