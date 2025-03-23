package com.Project_Group2.repository;

import com.Project_Group2.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog,Integer> {
    List<Blog> findTop4ByOrderByBlogIdDesc();
    List<Blog> findTop4ByBlogIdNotOrderByBlogIdDesc(int blogId);
    List<Blog> findAllByOrderByCreatedAtDesc();
    List<Blog> findAllByOrderByCreatedAtAsc();
}
