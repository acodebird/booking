package com.booking.dto;

import com.booking.domain.Comment;
import com.booking.domain.Hotel;
import com.booking.domain.Room;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class HotelDetailDTO {
    private Hotel hotel;
    private Comment comment;
    private List<Room> rooms = new ArrayList<>();
}
