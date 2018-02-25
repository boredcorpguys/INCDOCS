package com.incdocs.user.services;

import com.indocs.model.domain.Entity;
import com.indocs.model.request.UserCreateRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/incdocs/admin")
public class AdminServices {

    @PostMapping("/create/company")
    public Entity createCompany() {
        return null;
    }

    @PostMapping("/create/user")
    public String createUser(@RequestParam(value = "id", required = true)UserCreateRequest userCreateRequest) {

        return null;
    }
}
