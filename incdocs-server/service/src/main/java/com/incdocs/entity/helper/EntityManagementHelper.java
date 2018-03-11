package com.incdocs.entity.helper;

import com.incdocs.entity.dao.EntityDAO;
import com.indocs.model.domain.Entity;
import com.indocs.model.domain.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("entityManagementHelper")
public class EntityManagementHelper {
    @Autowired
    @Qualifier("entityDAO")
    private EntityDAO entityDAO;

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
}
