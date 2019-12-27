package com.booking.repository;

import com.booking.domain.Hotel;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository extends JpaSpecificationExecutor<Hotel>, PagingAndSortingRepository<Hotel, Long> {
	
}
