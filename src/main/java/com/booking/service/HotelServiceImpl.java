package com.booking.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    @Transactional
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
    public Page<Hotel> findAll(Specification<Hotel> specification, Pageable pageable) {
        return hotelRepository.findAll(specification, pageable);
    }
    /**
     * 根据id删除酒店
     */
	@Override
	@Transactional
	public void deleteById(Long hid) {
		hotelRepository.deleteById(hid);
	}
	 /**
     * 批量删除酒店
	 */
	@Override
	@Transactional
	public void deleteAll(List<Long> ids) {
		Iterable<Hotel> hotels = hotelRepository.findAllById(ids);
		if(hotels!=null) {
			hotelRepository.deleteAll(hotels);
		}
	}
	/**
     * 获取所有酒店信息
     * @return
     */
	@Override
	public List<Hotel> findAll() {
		return (List<Hotel>) hotelRepository.findAll();
	}
}
