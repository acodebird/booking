package com.booking.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.booking.domain.Hotel;

public interface HotelService {
	/**
     *  更新或者添加酒店
     */
    void save(Hotel hotel);
    /**
     * 根据id获取酒店信息
     */
    Hotel findById(Long hid);
    /**
     * 获取一页酒店信息
     */
    Page<Hotel> findAll(Pageable pageable);
    /**
     * 根据id删除酒店
     */
    void deleteById(Long hid);
    /**
     * 批量删除酒店
	 */
    void deleteAll(List<Long> ids);
}
