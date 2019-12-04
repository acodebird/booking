package com.booking.service;

import com.booking.domain.Hotel;
import com.booking.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HotelServiceImpl implements HotelService {
    @Autowired
    private HotelRepository hotelRepository;

    @Override
    public void save(Hotel hotel) {
        hotelRepository.save(hotel);
    }

    @Override
    public Hotel findById(Long hid) {
        return hotelRepository.findById(hid).get();
    }

    @Override
    public Iterable<Hotel> findAll() {
        return hotelRepository.findAll();
    }
}
