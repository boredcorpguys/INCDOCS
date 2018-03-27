package com.incdocs.entity.helper;

import com.incdocs.cache.AppCacheManager;
import com.incdocs.cache.CacheName;
import com.incdocs.entitlement.helper.EntitlementManagementHelper;
import com.incdocs.entity.dao.EntityDAO;
import com.incdocs.model.constants.ApplicationConstants;
import com.incdocs.model.domain.Entity;
import com.incdocs.model.domain.Role;
import com.incdocs.model.domain.User;
import com.incdocs.model.domain.UserEntitlement;
import com.incdocs.model.request.CreateCompanyRequest;
import com.incdocs.user.helper.UserManagementHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Component("entityManagementHelper")
public class EntityManagementHelper {

    @Autowired
    private AppCacheManager appCacheManager;

    @Autowired
    private EntityDAO entityDAO;

    @Autowired
    private EntitlementManagementHelper entitlementHelper;

    @Autowired
    private UserManagementHelper userManagementHelper;

   public Entity getEntity(String id) {
        Entity entity = appCacheManager.getValue(CacheName.ENTITY, id);
        if (entity == null) {
            try {
                entity = entityDAO.getEntity(id);
                appCacheManager.put(CacheName.ENTITY, entity.getEntityID(), entity);
            } catch (EmptyResultDataAccessException ex) {
                ex.printStackTrace();
            }
        }
        return entity;
    }

    public List<Role> getEntityRoles(String id) {
        List<Role> entityRoles = appCacheManager.getValue(CacheName.ENTITY_ROLES, id);
        if (CollectionUtils.isEmpty(entityRoles)) {
            entityRoles = entityDAO.getEntityRoles(id);
            appCacheManager.put(CacheName.ENTITY_ROLES, id, entityRoles);
        }
        return entityRoles;
    }

    public List<Entity> getEntitiesByName(String name) {
        return entityDAO.getEntityByName(name);
    }

    @Transactional
    public boolean createCompany(String adminID, CreateCompanyRequest createCompanyRequest) {
        int rows = entityDAO.createCompany(createCompanyRequest);
        entityDAO.createCompanyRoles(createCompanyRequest.getId(), entitlementHelper.getRoles(true));
        boolean done = rows == 1;
        if (done) {
            //cache it
            getEntity(createCompanyRequest.getId());
            getEntityRoles(createCompanyRequest.getId());
        }
        return done;
    }
}
