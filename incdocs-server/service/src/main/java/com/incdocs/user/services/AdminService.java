package com.incdocs.user.services;

import com.incdocs.entity.helper.EntityManagementHelper;
import com.incdocs.user.helper.UserManagementHelper;
import com.incdocs.utils.ApplicationException;
import com.indocs.model.request.CreateCompanyRequest;
import com.indocs.model.request.CreateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/incdocs/admin")
public class AdminService {

    @Autowired
    @Qualifier("userManagementHelper")
    private UserManagementHelper userManagementHelper;

    @Autowired
    @Qualifier("entityManagementHelper")
    private EntityManagementHelper entityManagementHelper;

    @PostMapping("/create/company")
    public boolean createCompany(@RequestHeader(value = "incdocsID") String adminID,
                                 @RequestBody CreateCompanyRequest createCompanyRequest) {
        return entityManagementHelper.createCompany(adminID, createCompanyRequest);
    }

    @PostMapping("/create/user")
    public String createUser(@RequestHeader(value = "incdocsID") String adminID,
                             @RequestBody CreateUserRequest userCreateRequest)
            throws ApplicationException {

        return userManagementHelper.createUser(adminID, userCreateRequest);
    }
}
