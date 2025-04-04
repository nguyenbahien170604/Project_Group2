package com.Project_Group2.repository;

import com.Project_Group2.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog,Integer> {
    List<Blog> findTop4ByOrderByBlogIdDesc();
    List<Blog> findTop4ByBlogIdNotOrderByBlogIdDesc(int blogId);
    List<Blog> findAllByOrderByCreatedAtDesc();
    List<Blog> findAllByOrderByCreatedAtAsc();
    @Query("SELECT b FROM Blog b WHERE b.isDeleted = false")
    Page<Blog> findAllActiveBlogs(Pageable pageable);

    // Find blogs by user id
    @Query("SELECT b FROM Blog b WHERE b.user.id = ?1 AND b.isDeleted = false")
    List<Blog> findByUserId(int userId);

    // Search blogs by title or content
    @Query("SELECT b FROM Blog b WHERE b.isDeleted = false " +
            "AND (COALESCE(:title, '') = '' OR b.title LIKE CONCAT('%', :title, '%')) " +
            "AND (COALESCE(:content, '') = '' OR b.content LIKE CONCAT('%', :content, '%')) ")
    Page<Blog> searchBlogs(@Param("title") String title,
                           @Param("content") String content,
                           Pageable pageable);


    // Find a single blog by id
    @Query("SELECT b FROM Blog b WHERE b.blogId = ?1 AND b.isDeleted = false")
    Blog findActiveBlogById(int id);
}
