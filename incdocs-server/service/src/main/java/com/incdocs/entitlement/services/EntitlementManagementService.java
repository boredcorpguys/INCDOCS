package com.incdocs.entitlement.services;

import com.incdocs.entitlement.dao.EntitlementDAO;
import com.incdocs.user.helper.UserManagementHelper;
import com.incdocs.utils.ApplicationException;
import com.indocs.model.domain.User;
import com.indocs.model.response.RoleActions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/incdocs/role")
public class EntitlementManagementService {

    @Autowired
    @Qualifier("entitlementDAO")
    private EntitlementDAO entitlementDAO;

    @Autowired
    @Qualifier("userManagementHelper")
    private UserManagementHelper userManagementHelper;

    @GetMapping("/actions")
    public @ResponseBody
    RoleActions getRoleActions(@RequestHeader(value = "incdocsID") String incdocsID) throws ApplicationException {
        User user = userManagementHelper.getUser(incdocsID);
        return entitlementDAO.getRoleActions(user.getRoleID());
    }
}
