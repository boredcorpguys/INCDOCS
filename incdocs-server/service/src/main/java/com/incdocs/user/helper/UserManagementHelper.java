package com.incdocs.user.helper;

import com.incdocs.cache.AppCacheManager;
import com.incdocs.entitlement.helper.EntitlementManagementHelper;
import com.incdocs.entity.helper.EntityManagementHelper;
import com.incdocs.model.constants.ApplicationConstants;
import com.incdocs.model.domain.Entity;
import com.incdocs.model.domain.Role;
import com.incdocs.model.domain.UserEntitlement;
import com.incdocs.user.dao.UserDAO;
import com.incdocs.utils.ApplicationException;
import com.incdocs.cache.CacheName;
import com.incdocs.model.domain.User;
import com.incdocs.model.request.CreateUserRequest;
import com.incdocs.model.request.UserProfileRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("userManagementHelper")
public class UserManagementHelper {
    @Autowired
    private AppCacheManager appCacheManager;

    @Autowired
    private EntityManagementHelper entityManagementHelper;

    @Autowired
    private UserDAO userManagementDAO;

    @Autowired
    private EntitlementManagementHelper entitlementManagementHelper;

    public int modifyUserDetails(UserProfileRequest user) {
        return userManagementDAO.modifyUserDetails(user);
    }

    public User getUser(String incdocsID) {
        User user = appCacheManager.getValue(CacheName.USER, incdocsID);
        if (user == null) {
            try {
                user = userManagementDAO.getUser(incdocsID);
                appCacheManager.put(CacheName.USER, user.getIncdocsID(), user);
            } catch (EmptyResultDataAccessException e) {
                // do nothing
            }
        }
        return user;
    }

    public UserEntitlement getUserEntitlements(String incdocsID) {
        UserEntitlement userEntitlement =
                appCacheManager.getValue(CacheName.USER_ENTITLEMENTS, incdocsID);
        if (userEntitlement == null) {
            try {
                userEntitlement = userManagementDAO.getUserEntitlements(incdocsID);
                appCacheManager.put(CacheName.USER_ENTITLEMENTS, incdocsID, userEntitlement);
            } catch (EmptyResultDataAccessException e) {
                // do nothing
            }
        }
        return userEntitlement;
    }

    public String createUser(String adminID, CreateUserRequest createUserRequest) throws ApplicationException {
        User admin = getUser(adminID);
        createUserRequest.setCompanyID(admin.getCompanyID());
        createUserRequest.setClient(admin.isClient());
        String incdocsID = userManagementDAO.createUser(createUserRequest);
        getUser(incdocsID); //cache it
        return incdocsID;
    }

    public List<Entity> getEntitledEntities(String incdocsID, boolean selfEntitled) {
        List<Entity> entities = new ArrayList<>();
        User user = getUser(incdocsID);
        Role role = entitlementManagementHelper.getRole(user.getRoleID());
        String whichUser = incdocsID;
        if (ApplicationConstants.Roles.valueOf(role.getRoleName())
                != ApplicationConstants.Roles.GROUP_HEAD
                && ApplicationConstants.Roles.valueOf(role.getRoleName())
                != ApplicationConstants.Roles.ADMIN
                && !selfEntitled) {
            // get gh entitled companies
            whichUser = user.getManagerID();
        }
        UserEntitlement userEntitlement = getUserEntitlements(whichUser);
        if (userEntitlement != null && !CollectionUtils.isEmpty(userEntitlement.getEntities())) {
            userEntitlement.getEntities()
                    .forEach(entityID -> entities.add(entityManagementHelper.getEntity(entityID)));
        }
        return entities;
    }
}
