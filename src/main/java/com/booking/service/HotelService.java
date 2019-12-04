package com.booking.service;

import com.booking.domain.Hotel;

import java.util.Optional;

public interface HotelService {
    void save(Hotel hotel);
    Hotel findById(Long hid);
    Iterable<Hotel> findAll();
}
