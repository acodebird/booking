package com.booking.service;

import com.booking.domain.Comment;
import com.booking.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService{
    @Autowired
    private CommentRepository commentRepository;
    public Comment save(Comment comment){
        return commentRepository.save(comment);
    }
    public void save(List<Comment> comments){
        commentRepository.saveAll(comments);
    }
    public void deleteAll(List<Comment> comments){
        commentRepository.deleteAll(comments);
    }
}
