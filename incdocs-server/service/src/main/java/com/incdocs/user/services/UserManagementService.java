package com.incdocs.user.services;

import com.incdocs.user.dao.UserManagementDAO;
import model.domain.Action;
import model.domain.Entity;
import model.domain.Role;
import model.domain.User;
import model.response.UserActionOnEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserManagementService {
    @Autowired
    @Qualifier("userManagementDAO")
    private UserManagementDAO userManagementDAO;


    @GetMapping("/actions")
    public @ResponseBody UserActionOnEntity get(@RequestParam(value="id", required=true) int id) {
        UserActionOnEntity response = new UserActionOnEntity();
        User u = new User().setId(123).setName("Vishnu");
        Role r = new Role().setRoleID(1).setRoleName("Admin");
        Action action1 = new Action().setActionID(1).setActionName("Bulk Upload");
        Action action2 = new Action().setActionID(1).setActionName("Add Company");
        Entity e1 = new Entity().setEntityID(1).setName("RIL");
        Entity e2 = new Entity().setEntityID(2).setName("RCOM");
        Entity e3 = new Entity().setEntityID(3).setName("Infosys");
        response.setUser(u);
        response.setActions(new Action[]{action1,action2});
        response.setEntities(new Entity[]{e1,e2});
        response.setRole(r);

        return response;
    }

}
