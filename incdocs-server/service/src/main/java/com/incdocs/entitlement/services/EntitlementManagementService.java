package com.incdocs.entitlement.services;

import com.incdocs.entitlement.dao.EntitlementDAO;
import com.indocs.model.domain.Role;
import com.indocs.model.response.RoleActions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/roles")
    public @ResponseBody
    List<Role> getRoles() {
        return entitlementDAO.getRoles();
    }
}