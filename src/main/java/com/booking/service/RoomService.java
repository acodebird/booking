package com.booking.service;

import java.util.List;

import com.booking.domain.Room;

public interface RoomService {
    void save(Room room);
    void deleteById(Long rid);
    Room findById(Long rid);
    List<Room> findAll();
}
