package com.booking.service;

import java.util.List;

import com.booking.domain.Hotel;

public interface HotelService {
    void save(Hotel hotel);
    void deleteById(Long hid);
    Hotel findById(Long hid);
    List<Hotel> findAll();
}
