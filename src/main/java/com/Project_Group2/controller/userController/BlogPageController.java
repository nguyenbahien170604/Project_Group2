package com.Project_Group2.controller.userController;

import com.Project_Group2.dto.BlogDTO;
import com.Project_Group2.entity.Blog;
import com.Project_Group2.service.BlogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BlogPageController {

    private final BlogService blogService;

    public BlogPageController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("/blog-details")
    public String blogPageDetail(Model model, @RequestParam("id") int blogId) {
        BlogDTO blog = blogService.getBlogById(blogId);
        model.addAttribute("blog", blog);
        return "user/blog-details";
    }
}
