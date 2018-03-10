package com.incdocs.user.services;

import com.incdocs.user.helper.UserManagementHelper;
import com.incdocs.utils.ApplicationException;
import com.indocs.model.domain.Entity;
import com.indocs.model.request.UserCreateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/incdocs/admin")
public class AdminServices {

    @Autowired
    @Qualifier("userManagementHelper")
    private UserManagementHelper userManagementHelper;

    @PostMapping("/create/company")
    public Entity createCompany() {
        return null;
    }

    @PostMapping("/create/user")
    public String createUser(@RequestHeader(value = "incdocsID") String adminID,
                             @RequestBody UserCreateRequest userCreateRequest)
            throws ApplicationException {

        return userManagementHelper.createUser(adminID, userCreateRequest);
    }
}
