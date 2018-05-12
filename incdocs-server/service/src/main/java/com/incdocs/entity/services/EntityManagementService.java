package com.incdocs.entity.services;

import com.incdocs.entity.helper.EntityManagementHelper;
import com.incdocs.model.domain.Entity;
import com.incdocs.model.domain.Role;
import com.incdocs.model.domain.User;
import com.incdocs.model.domain.UserEntitlement;
import com.incdocs.model.response.Response;
import com.incdocs.user.helper.UserManagementHelper;
import com.incdocs.utils.ApplicationException;
import com.incdocs.utils.Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    Response searchEntities(@RequestHeader(value = "incdocsID") String incdocsID,
                            @RequestParam(value = "name", required = false) String name,
                            @RequestParam(value = "pan", required = false) String pan,
                            @RequestParam(value = "gh", required = false) String ghID) throws ApplicationException {
        List<Entity> entities = new ArrayList<>();
        if (StringUtils.isNotEmpty(name))
            entities.addAll(entityManagementHelper.getEntitiesByName(name));
        else if (StringUtils.isNotEmpty(pan))
            entities.addAll(entityManagementHelper.getEntitiesByPan(pan));
        else {
            User user = userManagementHelper.getUser(incdocsID);
            String ghIncdocsID = Utils.idGenerator(user.getCompanyID(), user.getManagerID());
            if (StringUtils.isNotEmpty(ghID)) {
                ghIncdocsID = Utils.idGenerator(user.getCompanyID(), ghID);
            }
            UserEntitlement userEntitlement = userManagementHelper.getUserEntitlements(ghIncdocsID);

            if (userEntitlement != null) {
                entities.addAll(userEntitlement.getEntities().stream()
                        .map(entityManagementHelper::getEntity)
                        .collect(Collectors.toList()));
            }

        }
        Response.Metadata metadata1 = new Response.Metadata()
                .setColDisplayName("Company Name")
                .setColName("companyName")
                .setColType("string")
                .setVisibility(true);
        Response.Metadata metadata2 = new Response.Metadata()
                .setColDisplayName("Group Head")
                .setColName("groupHeadName")
                .setColType("string")
                .setVisibility(true);
        Response.Metadata metadata3 = new Response.Metadata()
                .setColName("companyId")
                .setColType("string")
                .setVisibility(false);
        Response.Metadata metadata4 = new Response.Metadata()
                .setColName("groupHeadId")
                .setColType("string")
                .setVisibility(false);

        entities.stream().map(entity -> userManagementHelper.getUserByEmpID(entity.getGroupHeadID()));

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
