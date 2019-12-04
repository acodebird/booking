package com.booking.repository;

import com.booking.domain.Room;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaSpecificationExecutor<Room>, PagingAndSortingRepository<Room, Long> {
}
