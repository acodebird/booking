package com.booking.repository;

import com.booking.domain.Comment;
import com.booking.domain.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaSpecificationExecutor<Comment>, PagingAndSortingRepository<Comment, Long> {
}
