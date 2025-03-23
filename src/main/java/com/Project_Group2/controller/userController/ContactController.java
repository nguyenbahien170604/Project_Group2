package com.Project_Group2.controller.userController;

import com.Project_Group2.entity.User;
import com.Project_Group2.repository.UserRepository;
import com.Project_Group2.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ContactController {

    @Autowired
    private EmailService emailService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/send-email")
    public String sendEmail(
            @RequestParam("name") String firstName,
            @RequestParam("lastname") String lastName,
            @RequestParam("email") String senderEmail,
            @RequestParam("subject") String subject,
            @RequestParam("message") String message,
            Model model) {

        List<User> list = userRepository.findByRole_Id(4L);
        if (list.isEmpty()) {
            model.addAttribute("errorMessage", "Admin email not found!");
            return "user/contact";
        }
        User admin = list.get(0);

        String fullMessage = "Sender: " + firstName + " " + lastName + "\nEmail: " + senderEmail + "\n\nMessage:\n" + message;

        // Gửi email từ hệ thống nhưng "Reply-To" là email người dùng nhập
        emailService.sendEmail(admin.getEmail(), subject, fullMessage);

        model.addAttribute("successMessage", "Your message has been sent successfully!");
        return "user/contact";
    }
}
