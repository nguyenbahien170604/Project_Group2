package com.Project_Group2.controller.userController;

import com.Project_Group2.entity.User;
import com.Project_Group2.repository.UserRepository;
import com.Project_Group2.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class AccountController {

    private final UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public AccountController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/account")
    public String myAccountPage(HttpSession session, Model model) {
        User sessionUser = (User) session.getAttribute("loggedInUser");

        if (sessionUser != null) {
            User updatedUser = userRepository.findById(sessionUser.getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            session.setAttribute("loggedInUser", updatedUser);
            model.addAttribute("user", updatedUser);
        } else {
            return "redirect:/login";
        }
        return "user/my-account";
    }

    @GetMapping("/change-information")
    public String showChangeInformationPage(Model model,HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        model.addAttribute("user", user);
        return "user/change-information"; // Trả về trang change-information.html
    }

    @PostMapping("/update-information")
    public String updateInformation(@RequestParam Integer id,
                                    @RequestParam String username,
                                    @RequestParam String phoneNumber,
                                    @RequestParam String address,
                                    RedirectAttributes redirectAttributes) {
        try {
            userService.updateUserInformation(id, username, phoneNumber, address);
            redirectAttributes.addFlashAttribute("successMessage", "Information updated successfully!");
            return "redirect:/account";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Update failed. Please try again.");
            return "redirect:/change-information";
        }
    }

    @GetMapping("/change-password")
    public String showChangePasswordPage() {
        return "user/change-password"; // Trả về trang Thymeleaf
    }

    @PostMapping("/update-password")
    public String updatePassword(
            @RequestParam("currentPassword") String currentPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmNewPassword") String confirmNewPassword,
            HttpSession session,
            Model model) {

        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login"; // Nếu chưa đăng nhập, chuyển về trang login
        }

        // Kiểm tra mật khẩu hiện tại có đúng không
        if (!passwordEncoder.matches(currentPassword, user.getPasswordHash())) {
            model.addAttribute("error", "Current password is incorrect.");
            return "user/change-password";
        }

        // Kiểm tra mật khẩu mới có khớp với xác nhận mật khẩu không
        if (!newPassword.equals(confirmNewPassword)) {
            model.addAttribute("error", "New passwords do not match.");
            return "user/change-password";
        }

        // Kiểm tra độ dài mật khẩu mới
        if (newPassword.length() < 6) {
            model.addAttribute("error", "New password must be at least 6 characters.");
            return "user/change-password";
        }

        // Cập nhật mật khẩu mới
        userService.updatePassword(user, newPassword);

        model.addAttribute("success", "Password updated successfully!");
        return "user/change-password";
    }

}
