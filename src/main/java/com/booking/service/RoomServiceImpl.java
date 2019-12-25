package com.booking.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.booking.domain.Room;
import com.booking.repository.RoomRepository;

@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    private RoomRepository roomRepository;

    @Override
    public void save(Room room) {
        roomRepository.save(room);
    }

    @Override
    public void deleteById(Long rid) {
        roomRepository.deleteById(rid);
    }

    @Override
    public Room findById(Long rid) {
        return roomRepository.findById(rid).get();
    }

    @Override
    public List<Room> findAll() {
        return (List<Room>) roomRepository.findAll();
    }

    @Override
    public List<Room> findByHid(Long hid) {
        return roomRepository.findByHid(hid);
    }
}
