package com.booking.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.booking.domain.Hotel;
import com.booking.repository.HotelRepository;

@Service
public class HotelServiceImpl implements HotelService {
    @Autowired
    private HotelRepository hotelRepository;

    /**
     *  更新或者添加酒店
     */
    @Override
    public void save(Hotel hotel) {
        hotelRepository.save(hotel);
    }
    /**
     * 根据id获取酒店信息
     */
    @Override
    public Hotel findById(Long hid) {
        return hotelRepository.findById(hid).get();
    }
    /**
     * 获取一页酒店信息
     */
    @Override
    public Page<Hotel> findAll(Pageable pageable) {
        return hotelRepository.findAll(pageable);
    }
    /**
     * 根据id删除酒店
     */
	@Override
	public void deleteById(Long hid) {
		hotelRepository.deleteById(hid);
	}
	 /**
     * 批量删除酒店
	 */
	@Override
	public void deleteAll(List<Long> ids) {
		Iterable<Hotel> hotels = hotelRepository.findAllById(ids);
		if(hotels!=null) {
			hotelRepository.deleteAll(hotels);
		}
	}
}
