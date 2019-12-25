package com.booking.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.booking.domain.Hotel;
import com.booking.domain.Room;

public interface RoomService {
    /**
     * 更新或者添加房间
     *
     * @param room
     */
    void save(Room room);

    /**
     * 根据id删除房间
     *
     * @param rid
     */
    void deleteById(Long rid);

    /*
     * 批量删除房间
     */
    void deleteAll(List<Long> ids);

    /**
     * 获取房间详情
     *
     * @param rid
     * @return
     */
    Room findById(Long rid);

    /**
     * 获取所有房间
     *
     * @return
     */
    List<Room> findAll();

    /**
     * 根据酒店id分页获取房间
     *
     * @param specification
     * @param pageable
     * @return
     */
    Page<Room> findByHid(Specification<Room> specification, Pageable pageable);

    /**
     * 根据酒店id获取房间
     *
     * @param hid
     * @return
     */
    List<Room> findByHid(Long hid);

    /**
     * 根据酒店id后去房间最低价
     *
     * @param hid
     * @return
     */
    Double getLandpriceByHid(Long hid);
}
