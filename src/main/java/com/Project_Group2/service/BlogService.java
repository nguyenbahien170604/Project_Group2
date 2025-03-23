package com.Project_Group2.service;

import com.Project_Group2.dto.BlogDTO;
import com.Project_Group2.entity.Blog;
import com.Project_Group2.entity.User;
import com.Project_Group2.repository.BlogRepository;
import com.Project_Group2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BlogService {
    private final UserRepository userRepository;
    private BlogRepository blogRepository;


    public BlogService(BlogRepository blogRepository, UserRepository userRepository) {
        this.blogRepository = blogRepository;
        this.userRepository = userRepository;
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

    public List<BlogDTO> getAllBlogsSortedByNewest() {
        return blogRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToDTO) // Gọi phương thức tự viết
                .collect(Collectors.toList());
    }

    public List<BlogDTO> getAllBlogsSortedByOldest() {
        return blogRepository.findAllByOrderByCreatedAtAsc()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Get all blogs with pagination
    public Page<Blog> getAllBlogsWithPagination(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by("createdAt").descending());
        return blogRepository.findAllActiveBlogs(pageable);
    }

    public Blog getBlogByIdBlog(int id) {
        return blogRepository.findActiveBlogById(id);
    }


    // Save a new blog
    public Blog saveBlog(BlogDTO blogDTO) {
        //User user = userRepository.findByUsername(blogDTO.getBlogUserName());
        User user = userRepository.findById(9).orElseThrow(() -> new RuntimeException("User not found"));

        Blog blog = new Blog();
        blog.setUser(user);
        blog.setTitle(blogDTO.getBlogTitle());
        blog.setContent(blogDTO.getBlogContent());
        blog.setShortDescription(blogDTO.getBlogShortDescription());
        blog.setImageUrl(blogDTO.getBlogImage());
        blog.setCreatedAt(new Date());
        blog.setUpdatedAt(new Date());
        blog.setDeleted(false);

        return blogRepository.save(blog);
    }

    // Update an existing blog
    public Blog updateBlog(BlogDTO blogDTO, int blogId) {
        Blog existingBlog = blogRepository.findById(blogId).orElseThrow(() ->
                new RuntimeException("Blog not found with id: " + blogId));

        existingBlog.setTitle(blogDTO.getBlogTitle());
        existingBlog.setContent(blogDTO.getBlogContent());
        existingBlog.setShortDescription(blogDTO.getBlogShortDescription());
        existingBlog.setImageUrl(blogDTO.getBlogImage());
        existingBlog.setUpdatedAt(new Date());

        return blogRepository.save(existingBlog);
    }

    // Delete blog (soft delete)
    public void deleteBlog(int id) {
        Blog blog = blogRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Blog not found with id: " + id));
        blog.setDeleted(true);
        blogRepository.save(blog);
    }

    // Search blogs
    public Page<Blog> searchBlogs(String keyword, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return blogRepository.searchBlogs(keyword, pageable);
    }

    // Get blogs by user id
    public List<Blog> getBlogsByUserId(int userId) {
        return blogRepository.findByUserId(userId);
    }
    public BlogDTO convertToDTO(Blog blog) {
        BlogDTO dto = new BlogDTO();
        dto.setId(blog.getBlogId());
        dto.setBlogTitle(blog.getTitle());
        dto.setBlogUserName(blog.getUser().getUsername());
        dto.setBlogShortDescription(blog.getShortDescription());
        dto.setBlogContent(blog.getContent());
        dto.setBlogImage(blog.getImageUrl());
        dto.setCreatedAt(blog.getCreatedAt());
        return dto;
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
