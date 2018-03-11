package com.incdocs.entitlement.services;

import com.incdocs.entitlement.helper.EntitlementHelper;
import com.incdocs.user.helper.UserManagementHelper;
import com.incdocs.utils.ApplicationException;
import com.indocs.model.domain.Role;
import com.indocs.model.domain.User;
import com.indocs.model.response.RoleActions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/incdocs/role")
public class EntitlementManagementService {

    @Autowired
    @Qualifier("entitlementHelper")
    private EntitlementHelper entitlementHelper;

    @Autowired
    @Qualifier("userManagementHelper")
    private UserManagementHelper userManagementHelper;

    @GetMapping("/actions")
    public @ResponseBody
    RoleActions getRoleActions(@RequestHeader(value = "incdocsID") String incdocsID) throws ApplicationException {
        User user = userManagementHelper.getUser(incdocsID);
        return entitlementHelper.getRoleActions(user.getRoleID());
    }

    @GetMapping("/all")
    public @ResponseBody
    List<Role>  getAllRoles(@RequestHeader(value = "incdocsID") String incdocsID) {
        User user = userManagementHelper.getUser(incdocsID);
        return entitlementHelper.getRoles(user.isClient());
    }
}
