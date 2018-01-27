package com.incdocs.user.services;

import com.incdocs.user.dao.UserManagementDAO;
import model.domain.Action;
import model.domain.Entity;
import model.domain.Role;
import model.domain.User;
import model.response.UserRoleActionsOnEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("incdocs/user")
public class UserManagementService {
    @Autowired
    @Qualifier("userManagementDAO")
    private UserManagementDAO userManagementDAO;


    @GetMapping("/actions")
    public @ResponseBody
    UserRoleActionsOnEntity get(@RequestParam(value="email_id", required=true) String emailId) {
        return userManagementDAO.getUserRolesActions(emailId);
    }

}
