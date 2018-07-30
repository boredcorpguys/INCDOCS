package com.incdocs.entitlement.helper;

import com.incdocs.cache.AppCacheManager;
import com.incdocs.cache.CacheName;
import com.incdocs.cache.CacheSearchAttributes;
import com.incdocs.entitlement.dao.EntitlementDAO;
import com.incdocs.model.constants.ApplicationConstants;
import com.incdocs.model.domain.Action;
import com.incdocs.model.response.RoleActions;
import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.expression.Criteria;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("entitlementHelper")
public class EntitlementManagementHelper {
    @Autowired
    private AppCacheManager appCacheManager;

    @Autowired
    @Qualifier("entitlementDAO")
    private EntitlementDAO entitlementDAO;

    public RoleActions getActionsForRole(Integer roleID) {
        com.incdocs.model.domain.Role role = getRole(roleID);
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

    public com.incdocs.model.domain.Role getRole(Integer roleID) {
        com.incdocs.model.domain.Role role = appCacheManager.getValue(CacheName.ROLE, roleID);
        if (role == null) {
            entitlementDAO.getRoleInfo(roleID);
            appCacheManager.put(CacheName.ROLE, roleID, role);
        }
        return role;
    }

    public List<com.incdocs.model.domain.Role> getRoles(boolean isClient) {
        Attribute<Boolean> isClientSearchAttr = appCacheManager.createSearchAttribute(CacheName.ROLE,
                CacheSearchAttributes.is_client);
        Criteria criteria = isClientSearchAttr.eq(isClient);
        return appCacheManager.queryCacheValues(CacheName.ROLE, criteria);
    }

    public com.incdocs.model.domain.Role getRole(ApplicationConstants.Role roleName) {
        Attribute<String> nameSearchAttr = appCacheManager.createSearchAttribute(CacheName.ROLE,
                CacheSearchAttributes.role_name);
        List<com.incdocs.model.domain.Role> roles = appCacheManager.queryCacheValues(CacheName.ROLE, nameSearchAttr.eq(roleName.name()));
        return roles.get(0);
    }
}
