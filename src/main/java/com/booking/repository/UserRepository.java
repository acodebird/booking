package com.booking.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.booking.domain.User;

@Repository
public interface UserRepository extends JpaSpecificationExecutor<User>,PagingAndSortingRepository<User, Long>{

}
