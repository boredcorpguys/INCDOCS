package com.incdocs.user.services;

import com.incdocs.user.dao.EntitlementDAO;
import model.domain.Role;
import model.response.RoleActions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/incdocs/entitlement")
public class EntitlementManagementService {

    @Autowired
    @Qualifier("entitlementDAO")
    private EntitlementDAO entitlementDAO;

    @GetMapping("/role")
    public @ResponseBody
    Role getRoleByID(@RequestParam(value="id", required=true) int id) {
        return entitlementDAO.getRoleInfo(id);
    }

    @GetMapping("/role/action")
    public @ResponseBody
    RoleActions getRoleActions(@RequestParam(value="id", required=true) int id) {
        return entitlementDAO.getRoleActions(id);
    }
}
