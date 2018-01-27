package com.incdocs.user.services;

import com.incdocs.user.dao.EntityDAO;
import model.domain.Entity;
import model.domain.ResourceGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/incdocs/org")
public class OrgManagementService {

    @Autowired
    @Qualifier("entityDAO")
    private EntityDAO entityDAO;

    @GetMapping("/entity")
    public @ResponseBody
    Entity getEntity(@RequestParam(value = "id", required = true) int id) {
        return entityDAO.getEntity(id);
    }

    @GetMapping("/rg")
    public @ResponseBody
    ResourceGroup getResourceGroup(@RequestParam(value = "id", required = true) int id) {
        return entityDAO.getResourceGroup(id);
    }
}
