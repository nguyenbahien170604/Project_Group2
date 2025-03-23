package com.Project_Group2.service;
import com.Project_Group2.entity.Comment;
import com.Project_Group2.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;


    public Page<Comment> getAllComments(Pageable pageable) {
        return commentRepository.findAllOrderByCreatedAtDesc(pageable);
    }

    public Page<Comment> searchComments(String keyword, Pageable pageable) {
        return commentRepository.findByContentContainingIgnoreCaseOrderByCreatedAtDesc(keyword, pageable);
    }
}
