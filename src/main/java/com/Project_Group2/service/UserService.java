package com.Project_Group2.service;

import com.Project_Group2.dto.UserDTO;
import com.Project_Group2.entity.Role;
import com.Project_Group2.entity.User;
import com.Project_Group2.repository.RoleRepository;
import com.Project_Group2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
}
