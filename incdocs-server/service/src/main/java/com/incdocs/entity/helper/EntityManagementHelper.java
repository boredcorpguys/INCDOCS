package com.incdocs.entity.helper;

import com.incdocs.entitlement.helper.EntitlementHelper;
import com.incdocs.entity.dao.EntityDAO;
import com.incdocs.user.helper.UserManagementHelper;
import com.indocs.model.domain.Entity;
import com.indocs.model.domain.Role;
import com.indocs.model.domain.User;
import com.indocs.model.request.CreateCompanyRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

@Component("entityManagementHelper")
public class EntityManagementHelper {
    @Autowired
    @Qualifier("entityDAO")
    private EntityDAO entityDAO;

    @Autowired
    @Qualifier("entitlementHelper")
    private EntitlementHelper entitlementHelper;

    @Cacheable(value = "entityCache", key = "#id")
    public Entity getEntity(String id) {
        Entity entity = null;
        try {
            entity = entityDAO.getEntity(id);
        } catch (EmptyResultDataAccessException ex) {
            ex.printStackTrace();
        }
        return entity;
    }

    @Cacheable(value = "entityRoleCache", key = "#id")
    public List<Role> getEntityRoles(String id) {
        return entityDAO.getEntityRoles(id);
    }

    public List<Entity> getEntitiesByName(String name) {
        return entityDAO.getEntityByName(name);
    }

    @Transactional
    public boolean createCompany(String adminID, CreateCompanyRequest createCompanyRequest) {
        int rows = entityDAO.createCompany(createCompanyRequest);
        entityDAO.createCompanyRoles(createCompanyRequest.getId(), entitlementHelper.getRoles(true));
        return rows == 1;
    }
}
