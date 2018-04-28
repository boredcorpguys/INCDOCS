package com.incdocs.entity.services;

import com.incdocs.entity.helper.EntityManagementHelper;
import com.incdocs.model.domain.Entity;
import com.incdocs.model.domain.Role;
import com.incdocs.model.domain.User;
import com.incdocs.user.helper.UserManagementHelper;
import com.incdocs.utils.ApplicationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
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
    List<Entity> getEntitiesByName(@RequestParam(value = "name", required = false) String name,
                                   @RequestParam(value = "pan", required = false) String pan) throws ApplicationException {
        if (StringUtils.isEmpty(name) && StringUtils.isEmpty(pan))
            throw new ApplicationException("query param should provide either name or pan", HttpStatus.BAD_REQUEST);
        if (StringUtils.isNotEmpty(name))
            return entityManagementHelper.getEntitiesByName(name);
        else
            return entityManagementHelper.getEntitiesByPan(pan);
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
