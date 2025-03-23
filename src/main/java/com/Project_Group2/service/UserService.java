package com.Project_Group2.service;

import com.Project_Group2.dto.ManagerDTO;
import com.Project_Group2.dto.UserDTO;
import com.Project_Group2.entity.Role;
import com.Project_Group2.entity.User;
import com.Project_Group2.repository.RoleRepository;
import com.Project_Group2.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User validateUser(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // So sánh mật khẩu nhập vào với mật khẩu đã mã hóa trong DB
            if (passwordEncoder.matches(password, user.getPasswordHash())) {
                return user; // Trả về user nếu đúng mật khẩu
            }
        }
        return null; // Trả về null nếu sai email hoặc mật khẩu
    }

    public User createUser(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUserName());

        // Mã hóa mật khẩu trước khi lưu
        user.setPasswordHash(passwordEncoder.encode(userDTO.getPassword()));

        user.setEmail(userDTO.getEmail());
        user.setAddress(userDTO.getAddress());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setAvatarUrl(userDTO.getAvatar());

        // Gán Role mặc định là User (ID = 2)
        Role defaultRole = roleRepository.findById(2)
                .orElseThrow(() -> new RuntimeException("Default role not found!"));
        user.setRole(defaultRole);

        return userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public void updateUserInformation(Integer id, String username, String phoneNumber, String address) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setUsername(username);
        user.setPhoneNumber(phoneNumber);
        user.setAddress(address);
        userRepository.save(user);
    }

    @Transactional
    public void updatePassword(User user, String newPassword) {
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void saveUserImg(HttpSession session,User user) {
        User updatedUser = userRepository.findById(user.getId()).orElse(null);
        if (updatedUser != null) {
            updatedUser.setAvatarUrl(user.getAvatarUrl());
            userRepository.save(updatedUser);

            // Cập nhật lại user trong session
            session.setAttribute("loggedInUser", updatedUser);
        }
    }

    public Page<User> getAllManagers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findUserByRole_Id(3, pageable);
    }

    public User addManager(ManagerDTO managerDTO,
                           RedirectAttributes redirectAttributes) {

        boolean hasError = false;

        if (userRepository.findUserByUsername(managerDTO.getUsername()) != null) {
            redirectAttributes.addFlashAttribute("error", "Username already exists!");
            hasError = true;
        }

        if (userRepository.findUserByEmail(managerDTO.getEmail()) != null) {
            redirectAttributes.addFlashAttribute("error", "Email already exists!");
            hasError = true;
        }

        if (userRepository.findUserByPhoneNumber(managerDTO.getPhone()) != null) {
            redirectAttributes.addFlashAttribute("error", "Phone number already exists!");
            hasError = true;
        }

        if (hasError) {
            return null;
        }

        User user = new User();
        user.setUsername(managerDTO.getUsername());
        user.setPasswordHash(passwordEncoder.encode(managerDTO.getPassword()));
        user.setEmail(managerDTO.getEmail());
        user.setPhoneNumber(managerDTO.getPhone());

        Role defaultRole = roleRepository.findById(3)
                .orElseThrow(() -> new RuntimeException("Default role not found!"));
        user.setRole(defaultRole);

        return userRepository.save(user);
    }

    public User findUserById(int id) {
        return userRepository.findUserById(id);
    }

    public User editManager(int id, ManagerDTO managerDTO, RedirectAttributes redirectAttributes) {
        boolean hasError = false;
        User currentManager = userRepository.findUserById(id);

        if (currentManager == null) {
            redirectAttributes.addFlashAttribute("error", "Manager does not exist");
            return null;
        }

        if (userRepository.findUserByUsername(managerDTO.getUsername()) != null &&
                !currentManager.getUsername().equals(managerDTO.getUsername())) {
            redirectAttributes.addFlashAttribute("error", "Username already exists");
            hasError = true;
        }

        if (userRepository.findUserByPhoneNumber(managerDTO.getPhone()) != null &&
                !currentManager.getPhoneNumber().equals(managerDTO.getPhone())) {
            redirectAttributes.addFlashAttribute("error", "Phone already exists");
            hasError = true;
        }

        if (userRepository.findUserByEmail(managerDTO.getEmail()) != null &&
                !currentManager.getEmail().equals(managerDTO.getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Email already exists");
            hasError = true;
        }

        if (hasError) {
            return null;
        }

        currentManager.setUsername(managerDTO.getUsername());
        currentManager.setPasswordHash(passwordEncoder.encode(managerDTO.getPassword()));
        currentManager.setEmail(managerDTO.getEmail());
        currentManager.setPhoneNumber(managerDTO.getPhone());
        currentManager.setDeleted(managerDTO.isDeleted());
        return userRepository.save(currentManager);
    }

    public Page<User> searchManagers(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        if (keyword == null || keyword.trim().isEmpty()) {
            return userRepository.findAll(pageable);
        }

        return userRepository.searchManagers(keyword, pageable);
    }

}
