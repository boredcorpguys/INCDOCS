package com.incdocs.entitlement.helper;

import com.incdocs.entitlement.dao.EntitlementDAO;
import com.indocs.cache.CacheName;
import com.indocs.model.domain.Role;
import com.indocs.model.response.RoleActions;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("entitlementHelper")
public class EntitlementHelper implements InitializingBean {
    @Autowired
    private org.springframework.cache.CacheManager cacheManager;

    private Cache roleActionCache;

    @Autowired
    @Qualifier("entitlementDAO")
    private EntitlementDAO entitlementDAO;

    @Override
    public void afterPropertiesSet() throws Exception {
        roleActionCache = cacheManager.getCache(CacheName.ROLE_ACTIONS.getName());
    }

    @Cacheable(value = "roleActionCache", key = "#roleID")
    public RoleActions getRoleActions(Integer roleID) {
        return entitlementDAO.getRoleActions(roleID);
    }

    public List<Role> getRoles(boolean isClient) {
        return entitlementDAO.getRoles(isClient);
    }
}
