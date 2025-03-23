package com.Project_Group2.repository;
import com.Project_Group2.entity.Comment;
import com.Project_Group2.entity.Product;
import com.Project_Group2.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query("SELECT c FROM Comment c ORDER BY c.createdAt DESC")
    Page<Comment> findAllOrderByCreatedAtDesc(Pageable pageable);

    Page<Comment> findByContentContainingIgnoreCaseOrderByCreatedAtDesc(String keyword, Pageable pageable);

    List<Comment> findByProductOrderByCreatedAtDesc(Product product);
    Comment findById(int id);
    List<Comment> findByUserOrderByCreatedAtDesc(User user);
    long countByProduct(Product product);
}