package com.booking.repository;

import com.booking.domain.Room;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaSpecificationExecutor<Room>, PagingAndSortingRepository<Room, Long> {
    @Query(value = "select * from t_room where hid = ?1 and stock > 0", nativeQuery = true)
    List<Room> findByHid(Long hid);

    @Query(value = "select min(price) from t_room where hid = ?1 and stock > 0", nativeQuery = true)
    Double getLandpriceByHid(Long hid);
}
