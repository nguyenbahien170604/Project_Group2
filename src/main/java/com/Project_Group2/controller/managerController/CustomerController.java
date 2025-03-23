package com.Project_Group2.controller.managerController;

import com.Project_Group2.entity.User;
import com.Project_Group2.service.CustomerService;
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
@RequestMapping("/manager/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public String listCustomers(Model model,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<User> customers = customerService.getAllCustomers(pageable);
        model.addAttribute("customers", customers);
        model.addAttribute("title", "Danh sách khách hàng");
        return "manager/customer";
    }


    @GetMapping("/search")
    public String searchCustomers(
            @RequestParam(name = "searchType", required = false, defaultValue = "name") String searchType,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<User> customers;

        if (keyword != null && !keyword.trim().isEmpty()) {
            if ("email".equals(searchType)) {
                customers = customerService.searchCustomersByEmail(keyword, pageable);
            } else {
                customers = customerService.searchCustomersByName(keyword, pageable);
            }
        } else {
            customers = customerService.getAllCustomers(pageable);
        }

        model.addAttribute("customers", customers);
        model.addAttribute("searchType", searchType);
        model.addAttribute("keyword", keyword);
        model.addAttribute("title", "Kết quả tìm kiếm");
        return "manager/customer";
    }
}