package com.Project_Group2.service;


import com.Project_Group2.entity.User;
import com.Project_Group2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final UserRepository userRepository;

    @Autowired
    public CustomerService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public Page<User> getAllCustomers(Pageable pageable) {
        return userRepository.findAllCustomer(2,pageable);
    }
    public Page<User> searchCustomersByName(String keyword, Pageable pageable) {
        return userRepository.findByUsernameLike(keyword, pageable);
    }


    public Page<User> searchCustomersByEmail(String keyword, Pageable pageable) {
        return userRepository.findByEmailLike(keyword, pageable);
    }
//    public List<User> getAllCustomers() {
//        return userRepository.findAllById(2);
//    }
//
//
//    public List<User> searchCustomersByName(String keyword) {
//        return userRepository.findByUsername(keyword);
//    }
//
//
//    public List<User> searchCustomersByEmail(String keyword) {
//        return userRepository.findByEmailLike(keyword);
//    }
//
//
//    public User getCustomerById(int id) {
//        return userRepository.findById(id);
//    }
}