package com.Project_Group2.controller.managerController;

import com.Project_Group2.dto.BlogDTO;
import com.Project_Group2.entity.Blog;
import com.Project_Group2.entity.User;
import com.Project_Group2.service.BlogService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequestMapping("/blogs")
public class BlogController {

    @Autowired
    private BlogService blogService;

    private static final String UPLOAD_DIR = "src/main/resources/static/assets/img/blog";

    // List all blogs with pagination
    @GetMapping("")
    public String listBlogs(Model model,
                            @RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "5") int size,
                            @RequestParam(required = false) String title,
                            @RequestParam(required = false) String keyword) {

        Page<Blog> blogPage;
        if (keyword != null && !keyword.isEmpty() || title != null && !title.isEmpty()) {
            blogPage = blogService.searchBlogs(title, keyword, page, size);
            model.addAttribute("keyword", keyword);
            model.addAttribute("title", title);
        } else {
            blogPage = blogService.getAllBlogsWithPagination(page, size);
        }

        model.addAttribute("blogs", blogPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", blogPage.getTotalPages());
        model.addAttribute("totalItems", blogPage.getTotalElements());

        return "manager/blog";
    }

    // Show blog details
    @GetMapping("/{id}")
    public String showBlog(@PathVariable("id") int id, Model model) {
        Blog blog = blogService.getBlogByIdBlog(id);
        if (blog == null) {
            return "redirect:/blogs?error=Blog+not+found";
        }
        model.addAttribute("blog", blog);
        return "manager/blogDetail";
    }

    // Show form to create a new blog
    @GetMapping("/new")
    public String showNewBlogForm(Model model, HttpSession session) {
        // Check if user is logged in
//        User currentUser = (User) session.getAttribute("currentUser");
//        if (currentUser == null) {
//            return "redirect:/login?error=Please+login+to+create+a+blog";
//        }

        model.addAttribute("blog", new BlogDTO());
        return "manager/blogForm";
    }

    // Save a new blog
    @PostMapping("/save")
    public String saveBlog(@ModelAttribute("blog") BlogDTO blogDTO,
                           @RequestParam("imageFile") MultipartFile imageFile,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {


        try {
            // Handle image upload if provided
            if (!imageFile.isEmpty()) {
                String filename = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
                Path fileNameAndPath = Paths.get(UPLOAD_DIR, filename);
                Files.createDirectories(fileNameAndPath.getParent());
                Files.write(fileNameAndPath, imageFile.getBytes());
                blogDTO.setBlogImage("assets/img/blog/" + filename);
            }

            blogService.saveBlog(blogDTO);
            redirectAttributes.addFlashAttribute("message", "Blog created successfully!");

            return "redirect:/blogs";

        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to upload image: " + e.getMessage());
            return "redirect:/blogs/new";
        }
    }

    // Show form to edit a blog
    @GetMapping("/edit/{id}")
    public String showEditBlogForm(@PathVariable("id") int id, Model model, HttpSession session) {
//        User currentUser = (User) session.getAttribute("currentUser");
//        if (currentUser == null) {
//            return "redirect:/login";
//        }

        Blog blog = blogService.getBlogByIdBlog(id);
        if (blog == null) {
            return "redirect:/blogs?error=Blog+not+found";
        }

//        // Check if the current user is the owner of the blog or an admin
//        if (blog.getUser().getId() != currentUser.getId() && !"ADMIN".equals(currentUser.getRole().getRoleName())) {
//            return "redirect:/blogs?error=You+are+not+authorized+to+edit+this+blog";
//        }

        BlogDTO blogDTO = blogService.convertToDTO(blog);
        model.addAttribute("blog", blogDTO);
        return "manager/blofEdit";
    }

    // Update an existing blog
    @PostMapping("/update/{id}")
    public String updateBlog(@PathVariable("id") int id,
                             @ModelAttribute BlogDTO blogDTO,
                             @RequestParam("imageFile") MultipartFile imageFile,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {

//        User currentUser = (User) session.getAttribute("currentUser");
//        if (currentUser == null) {
//            return "redirect:/login";
//        }

        Blog existingBlog = blogService.getBlogByIdBlog(id);
        if (existingBlog == null) {
            return "redirect:/blogs?error=Blog+not+found";
        }

//        // Check if the current user is the owner of the blog or an admin
//        if (existingBlog.getUser().getId() != currentUser.getId() && !"ADMIN".equals(currentUser.getRole().getRoleName())) {
//            return "redirect:/blogs?error=You+are+not+authorized+to+edit+this+blog";
//        }

        try {
            // Handle image upload if provided
            if (!imageFile.isEmpty()) {
                String filename = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
                Path fileNameAndPath = Paths.get(UPLOAD_DIR, filename);
                Files.createDirectories(fileNameAndPath.getParent());
                Files.write(fileNameAndPath, imageFile.getBytes());
                blogDTO.setBlogImage("assets/img/blog/" + filename);
            } else {
                // Keep the existing image
                blogDTO.setBlogImage(existingBlog.getImageUrl());
            }

            blogService.updateBlog(blogDTO, id);
            redirectAttributes.addFlashAttribute("message", "Blog updated successfully!");

            return "redirect:/blogs";

        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to upload image: " + e.getMessage());
            return "redirect:/blogs/edit/" + id;
        }
    }

    // Delete a blog
    @GetMapping("/delete/{id}")
    public String deleteBlog(@PathVariable("id") int id,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {

//        User currentUser = (User) session.getAttribute("currentUser");
//        if (currentUser == null) {
//            return "redirect:/login";
//        }

        Blog blog = blogService.getBlogByIdBlog(id);
        if (blog == null) {
            return "redirect:/blogs?error=Blog+not+found";
        }

        // Check if the current user is the owner of the blog or an admin
//        if (blog.getUser().getId() != currentUser.getId() && !"ADMIN".equals(currentUser.getRole().getRoleName())) {
//            return "redirect:/blogs?error=You+are+not+authorized+to+delete+this+blog";
//        }

        blogService.deleteBlog(id);
        redirectAttributes.addFlashAttribute("message", "Blog deleted successfully!");

        return "redirect:/blogs";
    }


}