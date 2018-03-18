package com.incdocs.entitlement.helper;

import com.incdocs.cache.AppCacheManager;
import com.incdocs.cache.CacheSearchAttributes;
import com.incdocs.entitlement.dao.EntitlementDAO;
import com.incdocs.cache.CacheName;
import com.incdocs.model.constants.ApplicationConstants;
import com.incdocs.model.domain.Action;
import com.incdocs.model.domain.Role;
import com.incdocs.model.response.RoleActions;
import com.incdocs.utils.ApplicationException;
import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.expression.Criteria;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("entitlementHelper")
public class EntitlementHelper {
    @Autowired
    private AppCacheManager appCacheManager;

    @Autowired
    @Qualifier("entitlementDAO")
    private EntitlementDAO entitlementDAO;

    public RoleActions getActionsForRole(Integer roleID) {
        Role role = getRole(roleID);
        List<Action> actionsForRole = appCacheManager.getValue(CacheName.ROLE_ACTIONS, roleID);
        if (CollectionUtils.isEmpty(actionsForRole)) {
            actionsForRole = entitlementDAO.getActionsForRole(roleID);
            appCacheManager.put(CacheName.ROLE_ACTIONS, roleID, actionsForRole);
        }
        RoleActions ra = new RoleActions();
        ra.setRole(role);
        actionsForRole.forEach(action -> ra.addAction(action));
        return ra;
    }

    public Role getRole(Integer roleID) {
        Role role = appCacheManager.getValue(CacheName.ROLE, roleID);
        if (role == null) {
            entitlementDAO.getRoleInfo(roleID);
            appCacheManager.put(CacheName.ROLE, roleID, role);
        }
        return role;
    }

    public List<Role> getRoles(boolean isClient) {
        Attribute<Boolean> isClientSearchAttr = appCacheManager.createSearchAttribute(CacheName.ROLE,
                CacheSearchAttributes.is_client);
        Criteria criteria = isClientSearchAttr.eq(isClient);
        return appCacheManager.queryCacheValues(CacheName.ROLE, criteria);
    }

    public Role getRole(ApplicationConstants.Roles roleName) {
        Attribute<String> nameSearchAttr = appCacheManager.createSearchAttribute(CacheName.ROLE,
                CacheSearchAttributes.role_name);
        List<Role> roles = appCacheManager.queryCacheValues(CacheName.ROLE, nameSearchAttr.eq(roleName.name()));
        return roles.get(0);
    }
}
