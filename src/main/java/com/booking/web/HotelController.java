package com.booking.web;

import java.util.*;

import com.booking.dto.HotelCriteriaQueryDTO;
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
import com.booking.dto.HotelDetailDTO;
import com.booking.dto.HotelQueryDTO;
import com.booking.service.HotelService;
import com.booking.service.RoomService;
import com.booking.utils.CopyPropertiesUtil;
import com.booking.utils.ResponseEntity;
import com.booking.utils.STablePageRequest;

@RestController
@RequestMapping(value = "/hotelManage")
public class HotelController {
    @Autowired
    private HotelService hotelService;

    @Autowired
    private RoomService roomService;

    /**
     * 获取所有酒店的品牌
     *
     * @return
     */
    @GetMapping("/getBrand")
    public ResponseEntity getHotelBrand() {
        Set<String> brands = new HashSet<>();
        hotelService.findAll().forEach(hotel -> brands.add(hotel.getBrand()));
        return ResponseEntity.ofSuccess().status(HttpStatus.OK).data(brands);
    }

    /**
     * 首页-获取酒店列表，多条件查询
     *
     * @param hotelCriteriaQueryDTO
     * @return
     */
    @PostMapping("/get")
    public ResponseEntity<Page<Hotel>> getHotels(@RequestBody HotelCriteriaQueryDTO hotelCriteriaQueryDTO) {
        Page<Hotel> page = hotelService.findAll(HotelCriteriaQueryDTO.getSpecification(hotelCriteriaQueryDTO), hotelCriteriaQueryDTO.getPageable());
        Iterator<Hotel> iterator = page.iterator();
        while (iterator.hasNext()) {
            Hotel hotel = iterator.next();
            Double landprice = roomService.getLandpriceByHid(hotel.getHid());
            if (null == hotel.getLandprice() || hotel.getLandprice() > landprice) {
                hotel.setLandprice(landprice);
                hotelService.save(hotel);
            }
        }

        return ResponseEntity.ofSuccess().status(HttpStatus.OK).data(page);
    }

    /*----------------------------------------------------------------------------------------------------------------*/

    /**
     * 获取一页酒店
     *
     * @param pageable
     * @return
     */
    @GetMapping
    public ResponseEntity<Page<Hotel>> getHotelPage(STablePageRequest pageable, HotelQueryDTO hotelQueryDTO) {
        if (StringUtils.isBlank(pageable.getSortField())) {
            pageable.setSortField("hid");
        }
        Page<Hotel> page = Page.empty(pageable.getPageable());
        page = hotelService.findAll(HotelQueryDTO.getSpecification(hotelQueryDTO), pageable.getPageable());
        Iterator<Hotel> iterator = page.iterator();
        while (iterator.hasNext()) {
            Hotel hotel = iterator.next();
            Double landprice = roomService.getLandpriceByHid(hotel.getHid());
            if (null == hotel.getLandprice() || hotel.getLandprice() > landprice) {
                hotel.setLandprice(landprice);
                hotelService.save(hotel);
            }
        }
        return ResponseEntity.ofSuccess().status(HttpStatus.OK).data(page);
    }

    /**
     * 根据id删除酒店信息
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity deleteHotelById(@PathVariable("id") Long id) {
        List<Room> rooms = roomService.findByHid(id);
        for (Room room : rooms) {
            roomService.deleteById(room.getRid());
        }
        hotelService.deleteById(id);
        return ResponseEntity.ofSuccess().status(HttpStatus.OK);
    }

    /**
     * 批量删除酒店信息
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public ResponseEntity deleteHotelByIds(Long[] ids) {
        for (Long id : ids) {
            List<Room> rooms = roomService.findByHid(id);
            for (Room room : rooms) {
                roomService.deleteById(room.getRid());
            }
        }
        hotelService.deleteAll(Arrays.asList(ids));
        return ResponseEntity.ofSuccess().status(HttpStatus.OK);
    }

    /**
     * 添加一条酒店信息
     *
     * @param hotel
     * @return
     */
    @PostMapping
    public ResponseEntity addHotel(@RequestBody Hotel hotel) {
        hotelService.save(hotel);
        return ResponseEntity.ofSuccess().status(HttpStatus.OK);
    }

    /**
     * 根据id获取酒店详细信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<HotelDetailDTO> getHotelDetail(@PathVariable("id") Long id) {
        Hotel hotel = hotelService.findById(id);
        List<Room> rooms = roomService.findByHid(hotel.getHid());
        HotelDetailDTO hotelDetailDTO = new HotelDetailDTO();
        hotelDetailDTO.setHotel(hotel);
        hotelDetailDTO.setRooms(rooms);

        return ResponseEntity.ofSuccess().status(HttpStatus.OK).data(hotelDetailDTO);
    }

    /**
     * 更新酒店信息
     *
     * @param hid
     * @param hotel
     * @return
     */
    @PutMapping("/{hid}")
    public ResponseEntity updateHotelImg(@PathVariable("hid") Long hid, @RequestBody Hotel hotel) {
        Hotel target = hotelService.findById(hid);
        BeanUtils.copyProperties(hotel, target, CopyPropertiesUtil.getNullPropertyNames(hotel));
        hotelService.save(target);
        return ResponseEntity.ofSuccess().status(HttpStatus.OK);
    }
}
