package com.incdocs.user.services;

import com.incdocs.user.dao.EntityDAO;
import model.domain.Entity;
import model.domain.ResourceGroup;
import model.domain.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/incdocs/org")
public class EntityManagementService {

    @Autowired
    @Qualifier("entityDAO")
    private EntityDAO entityDAO;

    @GetMapping("/entity/id")
    public @ResponseBody
    Entity getEntity(@RequestParam(value = "id", required = true) String id) {
        return entityDAO.getEntity(id);
    }

    @GetMapping("/entity/name")
    public @ResponseBody
    List<Entity> getEntitiesByName(@RequestParam(value = "name", required = true) String name) {
        return entityDAO.getEntityByName(name);
    }

    @GetMapping("/entity/roles")
    public @ResponseBody
    List<Role> getEntitiesRoles(@RequestParam(value = "id", required = true) String id) {
        return entityDAO.getEntityRoles(id);
    }
}
