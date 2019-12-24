package com.booking.service;

import com.booking.domain.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> findAllByOid(Long oid);

    public Comment save(Comment comment);

    public void save(List<Comment> comments);

    public void deleteAll(List<Comment> comments);
}
