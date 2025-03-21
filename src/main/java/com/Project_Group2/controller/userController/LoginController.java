package com.Project_Group2.controller.userController;

import com.Project_Group2.entity.Role;
import com.Project_Group2.entity.User;
import com.Project_Group2.repository.RoleRepository;
import com.Project_Group2.repository.UserRepository;
import com.Project_Group2.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

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

    @GetMapping("/login")
    public String showLoginPage() {
        return "user/login"; // Trả về giao diện login
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String email,
                               @RequestParam String password,
                               Model model,
                               HttpSession session) {
        User user = userService.validateUser(email, password);

        if (user == null) {
            model.addAttribute("error", "Invalid email or password!");
            return "user/login";
        }

        session.setAttribute("loggedInUser", user); // Lưu user vào session
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
                                  @RequestParam String address,
                                  @RequestParam String phoneNumber,
                                  Model model) {
        if (userName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            model.addAttribute("error", "Please fill in all required fields!");
            return "user/register";
        }

        if (userRepository.findByEmail(email).isPresent()) {
            model.addAttribute("error", "Email is already in use!");
            return "user/register";
        }

//        String avatarPath = "";
//        if (!avatar.isEmpty()) {
//            try {
//                String uploadDir = "uploads/avatar/";
//                File dir = new File(uploadDir);
//                if (!dir.exists()) {
//                    dir.mkdirs();
//                }
//
//                avatarPath = "/uploads/avatar/" + avatar.getOriginalFilename();
//                avatar.transferTo(new File(uploadDir + avatar.getOriginalFilename()));
//            } catch (IOException e) {
//                model.addAttribute("error", "Failed to upload avatar!");
//                return "user/register";
//            }
//        }

        Role userRole = roleRepository.findById(2).orElseThrow(() -> new RuntimeException("Default role not found"));
        model.addAttribute("error", "Register successfully");
        User newUser = new User(userName, passwordEncoder.encode(password), email, address, phoneNumber, null);
        newUser.setRole(userRole);

        userRepository.save(newUser);

        return "redirect:/login";
    }
}
