package com.incdocs.config.initializer;

import com.incdocs.cache.AppCacheManager;
import com.incdocs.cache.CacheName;
import com.incdocs.entitlement.dao.EntitlementDAO;
import com.incdocs.model.domain.Role;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoleCacheInitializer implements InitializingBean {
    @Autowired
    private AppCacheManager appCacheManager;

    @Autowired
    private EntitlementDAO entitlementDAO;

    @Override
    public void afterPropertiesSet() throws Exception {
        List<Role> allRoles = entitlementDAO.getAllRoles();
        if (CollectionUtils.isNotEmpty(allRoles)) {
            allRoles.forEach(role -> appCacheManager.put(CacheName.ROLE, role.getRoleID(), role));
        }
    }
}
