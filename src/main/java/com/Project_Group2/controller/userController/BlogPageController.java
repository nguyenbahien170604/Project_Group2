package com.Project_Group2.controller.userController;

import com.Project_Group2.dto.BlogDTO;
import com.Project_Group2.entity.Blog;
import com.Project_Group2.service.BlogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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
        List<Blog> top4blogs = blogService.get4BlogNotExist(blogId);

        model.addAttribute("top4blogs", top4blogs);
        return "user/blog-details";
    }

    @GetMapping("/blogSort")
    public String getBlogPageSort(@RequestParam(name = "sort", defaultValue = "newest") String sort, Model model) {
        List<BlogDTO> blogDTOList;
        List<Blog> top4BlogList = blogService.get4Blog();
        if ("oldest".equals(sort)) {
            blogDTOList = blogService.getAllBlogsSortedByOldest();
        } else {
            blogDTOList = blogService.getAllBlogsSortedByNewest();
        }
        model.addAttribute("Top4BlogList",top4BlogList);
        model.addAttribute("blogDTOList", blogDTOList);
        model.addAttribute("sort", sort); // Gửi giá trị sort về để hiển thị đúng option trong select
        return "user/blog";
    }
}
