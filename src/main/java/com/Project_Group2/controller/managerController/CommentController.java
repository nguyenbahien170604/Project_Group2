package com.Project_Group2.controller.managerController;

import com.Project_Group2.entity.Comment;
import com.Project_Group2.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/")
    public String showAllComments(Model model,
                                  @RequestParam(required = false) String keyword,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> commentPage;

        if (keyword != null && !keyword.isEmpty()) {
            commentPage = commentService.searchComments(keyword, pageable);
            model.addAttribute("keyword", keyword);
        } else {
            commentPage = commentService.getAllComments(pageable);
        }

        model.addAttribute("comments", commentPage);
        return "manager/comments";
    }


}