package com.Project_Group2.controller.userController;

import com.Project_Group2.entity.Role;
import com.Project_Group2.entity.User;
import com.Project_Group2.repository.RoleRepository;
import com.Project_Group2.repository.UserRepository;
import com.Project_Group2.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

//    @GetMapping("/login")
//    public String showLoginPage() {
//        return "user/login"; // Trả về giao diện login
//    }
//
//    @PostMapping("/login")
//    public String processLogin(@RequestParam String email,
//                               @RequestParam String password,
//                               Model model,
//                               HttpSession session) {
//        User user = userService.validateUser(email, password);
//
//        if (user == null) {
//            model.addAttribute("error", "Invalid email or password!");
//            return "user/login";
//        }
//        if(user.getRole().getId() == 2) {
//            session.setAttribute("loggedInUser", user);
//        }else if(user.getRole().getId() == 3) {
//            session.setAttribute("currentUser", user);
//        }
//        return "redirect:/";
//    }

    @GetMapping("/login")
    public String showLoginPage(HttpServletRequest request, Model model) {
        // Đọc cookie nếu có
        String rememberedEmail = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("rememberedEmail")) {
                    rememberedEmail = cookie.getValue();
                    break;
                }
            }
        }
        model.addAttribute("rememberedEmail", rememberedEmail);
        return "user/login"; // Trả về giao diện login
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String email,
                               @RequestParam String password,
                               @RequestParam(value = "rememberMe", required = false) String rememberMe,
                               Model model,
                               HttpSession session,
                               HttpServletResponse response) {
        User user = userService.validateUser(email, password);

        if (user == null) {
            model.addAttribute("error", "Invalid email or password!");
            return "user/login";
        }

        // Xử lý Remember Me
        if (rememberMe != null) { // Nếu chọn "Remember Me"
            Cookie cookie = new Cookie("rememberedEmail", email);
            cookie.setMaxAge(1 * 24 * 60 * 60);
            cookie.setPath("/");
            response.addCookie(cookie);
        } else { // Nếu không chọn, xóa cookie
            Cookie cookie = new Cookie("rememberedEmail", "");
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        // Set session cho user
        if (user.getRole().getId() == 2) {
            session.setAttribute("loggedInUser", user);
        } else if (user.getRole().getId() == 3) {
            session.setAttribute("currentUser", user);
        }

        return "redirect:/";
    }



    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("loggedInUser");
        return "user/login";
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "user/register";
    }

    @PostMapping("/register")
    public String processRegister(@RequestParam String userName,
                                  @RequestParam String email,
                                  @RequestParam String password,
                                  @RequestParam String confirmPassword,
                                  @RequestParam String address,
                                  @RequestParam String phoneNumber,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        // Kiểm tra các trường rỗng
        if (userName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            model.addAttribute("error", "Please fill in all required fields!");
            return "user/register";
        }

        // Kiểm tra mật khẩu nhập lại
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match!");
            return "user/register";
        }

        // Kiểm tra email đã tồn tại chưa
        if (userRepository.findByEmail(email).isPresent()) {
            model.addAttribute("error", "Email is already in use!");
            return "user/register";
        }

        // Lấy role mặc định
        Role userRole = roleRepository.findById(2).orElseThrow(() -> new RuntimeException("Default role not found"));

        // Tạo user mới
        User newUser = new User(userName, passwordEncoder.encode(password), email, address, phoneNumber, null);
        newUser.setRole(userRole);
        userRepository.save(newUser);

        // Thêm thông báo thành công
        redirectAttributes.addFlashAttribute("success", "Register successfully! Please login.");

        return "redirect:/login";
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordPage() {
        return "user/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam String email, Model model) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setPasswordHash(passwordEncoder.encode("123456")); // Đặt lại mật khẩu
            userRepository.save(user);
            model.addAttribute("message", "Your password has been reset to '123456'. Please log in and change it.");
        } else {
            model.addAttribute("error", "Email not found. Please try again.");
        }

        return "user/forgot-password";
    }
}
