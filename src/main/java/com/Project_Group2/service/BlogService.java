package com.Project_Group2.service;

import com.Project_Group2.dto.BlogDTO;
import com.Project_Group2.entity.Blog;
import com.Project_Group2.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BlogService {
    private BlogRepository blogRepository;

    public BlogService(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    // Lấy tất cả blog (chỉ lấy những blog chưa bị xóa)
    public List<BlogDTO> getAllBlogs() {
        List<Blog> blogs = blogRepository.findAll()
                .stream()
                .filter(blog -> !blog.getDeleted()) // Lọc các blog chưa bị xóa
                .collect(Collectors.toList());

        return blogs.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<Blog> get4Blog(){
        List<Blog> blogs = blogRepository.findTop4ByOrderByBlogIdDesc();
        return blogs;
    }

    public List<Blog> get4BlogNotExist(int id){
        List<Blog> blogs = blogRepository.findTop4ByBlogIdNotOrderByBlogIdDesc(id);
        return blogs;
    }

    // Lấy blog theo ID
    public BlogDTO getBlogById(int blogId) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new RuntimeException("Blog not found"));

        return mapToDTO(blog);
    }

    // Thêm blog mới
    public BlogDTO createBlog(BlogDTO blogDTO) {
        Blog blog = new Blog();
        blog.setTitle(blogDTO.getBlogTitle());
        blog.setShortDescription(blogDTO.getBlogShortDescription());
        blog.setContent(blogDTO.getBlogContent());
        blog.setImageUrl(blogDTO.getBlogImage());
        blog.setDeleted(false); // Blog mặc định chưa bị xóa

        Blog savedBlog = blogRepository.save(blog);
        return mapToDTO(savedBlog);
    }

    // Cập nhật blog
    public BlogDTO updateBlog(int blogId, BlogDTO blogDTO) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new RuntimeException("Blog not found"));

        blog.setTitle(blogDTO.getBlogTitle());
        blog.setShortDescription(blogDTO.getBlogShortDescription());
        blog.setContent(blogDTO.getBlogContent());
        blog.setImageUrl(blogDTO.getBlogImage());

        Blog updatedBlog = blogRepository.save(blog);
        return mapToDTO(updatedBlog);
    }

    // Xóa blog (Đánh dấu isDeleted = true)
    public void deleteBlog(int blogId) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new RuntimeException("Blog not found"));

        blog.setDeleted(true);
        blogRepository.save(blog);
    }

    // Chuyển từ Entity -> DTO
    private BlogDTO mapToDTO(Blog blog) {
        return new BlogDTO(
                blog.getBlogId(),
                blog.getTitle(),
                blog.getUser() != null ? blog.getUser().getUsername() : "Unknown",
                blog.getShortDescription(),
                blog.getContent(),
                blog.getImageUrl(),
                blog.getCreatedAt()
        );
    }
}
