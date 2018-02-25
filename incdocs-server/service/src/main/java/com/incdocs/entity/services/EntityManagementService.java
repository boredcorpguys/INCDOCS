package com.incdocs.entity.services;

import com.incdocs.entity.helper.EntityManagementHelper;
import com.indocs.model.domain.Entity;
import com.indocs.model.domain.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/incdocs/org")
public class EntityManagementService {

    @Autowired
    @Qualifier("entityManagementHelper")
    private EntityManagementHelper entityManagementHelper;

    /**
     * get entity by id
     * @param id
     * @return
     */
    @GetMapping("/entity/id")
    public @ResponseBody
    Entity getEntity(@RequestParam(value = "id", required = true) String id) {
        return entityManagementHelper.getEntity(id);
    }

    /**
     * entities list by name regex
     * @param name
     * @return
     */
    @GetMapping("/entity/name")
    public @ResponseBody
    List<Entity> getEntitiesByName(@RequestParam(value = "name", required = true) String name) {
        return entityManagementHelper.getEntitiesByName(name);
    }

    /**
     * set of roles available for an enitity
     * ICICI has Admin, GroupHead, RelationshipManager and so on...
     * @param id
     * @return
     */
    @GetMapping("/entity/roles")
    public @ResponseBody
    List<Role> getEntitiesRoles(@RequestParam(value = "id", required = true) String id) {
        return entityManagementHelper.getEntityRoles(id);
    }
}
