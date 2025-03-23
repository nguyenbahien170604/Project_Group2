package com.Project_Group2.controller.adminController;

import com.Project_Group2.dto.ManagerDTO;
import com.Project_Group2.entity.User;
import com.Project_Group2.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ManagerListController {

    @Autowired
    private UserService userService;

    @GetMapping("/managerList")
    public String managerList(@RequestParam(value = "search", required = false) String search,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              Model model) {

        Page<User> managers;

        if (search != null && !search.trim().isEmpty()) {
            managers = userService.searchManagers(search, page, size);
        } else {
            managers = userService.getAllManagers(page, size);
        }

        model.addAttribute("allManagers", managers.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", managers.getTotalPages());
        model.addAttribute("search", search);
        return "admin/managerList";
    }

    @GetMapping("/addManager")
    public String addManager() {
        return "admin/addManager";
    }

    @PostMapping("/addManager")
    public String createStore(@Valid ManagerDTO managerDTO,
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                redirectAttributes.addFlashAttribute("error", fieldError.getDefaultMessage());
            }
            return "redirect:/addManager";
        }

        try {
            User manager = userService.addManager(managerDTO, redirectAttributes);
            if (manager == null) {
                return "redirect:/addManager";
            }
            model.addAttribute("manager", manager);
            redirectAttributes.addFlashAttribute("success", "Added manager successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An error occurred, please try again!");
        }
        return "redirect:/managerList";
    }

    @GetMapping("/editManager/{id}")
    public String editManager(@PathVariable int id, Model model) {
        User manager = userService.findUserById(id);
        if (manager == null) {
            return "redirect:/managerList";
        }
        model.addAttribute("manager", manager);
        return "admin/editManager";
    }

    @PostMapping("/editManager")
    public String updateStore(@RequestParam int id,
                              @Valid ManagerDTO managerDTO,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                redirectAttributes.addFlashAttribute("error", fieldError.getDefaultMessage());
            }
            return "redirect:/editManager/" + id;
        }

        try {
            User manager = userService.editManager(id, managerDTO, redirectAttributes);
            if (manager == null) {
                return "redirect:/editManager/" + id;
            }
            redirectAttributes.addFlashAttribute("success", "Manager update successful!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An error occurred, please try again!");
        }
        return "redirect:/managerList";
    }

}
