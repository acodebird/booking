package com.booking.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.booking.domain.Hotel;
import com.booking.domain.Room;
import com.booking.repository.RoomRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    private RoomRepository roomRepository;

    /**
     * 更新或者添加房间
     *
     * @param room
     */
    @Override
    @Transactional
    public void save(Room room) {
        roomRepository.save(room);
    }

    /**
     * 根据id删除房间
     *
     * @param rid
     */
    @Override
    @Transactional
    public void deleteById(Long rid) {
        roomRepository.deleteById(rid);
    }

    /**
     * 批量删除房间
     * @param ids
     */
    @Override
    @Transactional
    public void deleteAll(List<Long> ids) {
        Iterable<Room> rooms = roomRepository.findAllById(ids);
        if (rooms != null) {
            roomRepository.deleteAll(rooms);
        }
    }

    /**
     * 获取房间详情
     *
     * @param rid
     * @return
     */
    @Override
    public Room findById(Long rid) {
        return roomRepository.findById(rid).get();
    }

    /**
     * 获取所有房间
     *
     * @return
     */
    @Override
    public List<Room> findAll() {
        return (List<Room>) roomRepository.findAll();
    }

    /**
     * 根据酒店id分页获取房间
     *
     * @param specification
     * @param pageable
     * @return
     */
    @Override
    public Page<Room> findByHid(Specification<Room> specification, Pageable pageable) {
        return roomRepository.findAll(specification, pageable);
    }

    /**
     * 根据酒店id获取房间
     *
     * @param hid
     * @return
     */
    @Override
    public List<Room> findByHid(Long hid) {
        return roomRepository.findByHid(hid);
    }

    /**
     * 根据酒店id获取房间最低价
     *
     * @param hid
     * @return
     */
    @Override
    public Double getLandpriceByHid(Long hid) {
        return roomRepository.getLandpriceByHid(hid);
    }
}
