package com.booking.dto;

import com.booking.domain.Comment;
import com.booking.domain.Hotel;
import com.booking.domain.Room;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 接口类：酒店详情的数据载体(未完成)
 */
@Data
public class HotelDetailDTO {
    private Hotel hotel;
    private Comment comment;
    private List<Room> rooms = new ArrayList<>();
}
