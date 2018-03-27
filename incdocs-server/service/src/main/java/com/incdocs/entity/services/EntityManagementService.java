package com.incdocs.entity.services;

import com.incdocs.entity.helper.EntityManagementHelper;
import com.incdocs.user.helper.UserManagementHelper;
import com.incdocs.utils.ApplicationException;
import com.incdocs.model.domain.Entity;
import com.incdocs.model.domain.Role;
import com.incdocs.model.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/incdocs/company")
public class EntityManagementService {

    @Autowired
    @Qualifier("entityManagementHelper")
    private EntityManagementHelper entityManagementHelper;

    @Autowired
    @Qualifier("userManagementHelper")
    private UserManagementHelper userManagementHelper;

    /**
     * get entity by id
     *
     * @param id
     * @return
     */
    @GetMapping("/detail")
    public @ResponseBody
    Entity getEntity(@RequestHeader(value = "incdocsID") String incdocsID) throws ApplicationException {
        User user = userManagementHelper.getUser(incdocsID);
        return entityManagementHelper.getEntity(user.getCompanyID());
    }

    /**
     * entities list by name regex
     *
     * @param name
     * @return
     */
    @GetMapping("/search")
    public @ResponseBody
    List<Entity> getEntitiesByName(@RequestParam(value = "name", required = true) String name) {
        return entityManagementHelper.getEntitiesByName(name);
    }

    /**
     * set of roles available for an enitity
     * ICICI has Admin, GroupHead, RelationshipManager and so on...
     *
     * @param id
     * @return
     */
    @GetMapping("/roles")
    public @ResponseBody
    List<Role> getEntitiesRoles(@RequestHeader(value = "incdocsID") String incdocsID) throws ApplicationException {
        Entity entity = getEntity(incdocsID);
        return entityManagementHelper.getEntityRoles(entity.getEntityID());
    }
}
