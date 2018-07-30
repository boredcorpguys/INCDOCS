package com.incdocs.user.helper;

import com.incdocs.cache.AppCacheManager;
import com.incdocs.cache.CacheName;
import com.incdocs.cache.CacheSearchAttributes;
import com.incdocs.entitlement.helper.EntitlementManagementHelper;
import com.incdocs.entity.helper.EntityManagementHelper;
import com.incdocs.model.constants.ApplicationConstants;
import com.incdocs.model.domain.Entity;
import com.incdocs.model.domain.User;
import com.incdocs.model.domain.UserEntitlement;
import com.incdocs.model.request.CreateUserRequest;
import com.incdocs.model.request.UserProfileRequest;
import com.incdocs.user.dao.UserDAO;
import com.incdocs.utils.ApplicationException;
import com.incdocs.utils.Utils;
import net.sf.ehcache.search.Attribute;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
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

    public User getUserByEmpID(String empID) {
        Attribute<String> idSearchAttr = appCacheManager.createSearchAttribute(CacheName.USER,
                CacheSearchAttributes.emp_id);
        List<User> users = appCacheManager.queryCacheValues(CacheName.USER, idSearchAttr.eq(empID));
        if (CollectionUtils.isNotEmpty(users))
            return users.get(0);
        return null;
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
        com.incdocs.model.domain.Role role = entitlementManagementHelper.getRole(user.getRoleID());
        ApplicationConstants.Role roleConstant = ApplicationConstants.Role.valueOf(role.getRoleName());
        String whichUser = incdocsID;
        if (roleConstant != ApplicationConstants.Role.GROUP_HEAD
                && roleConstant != ApplicationConstants.Role.ADMIN
                && !selfEntitled) {
            // get gh entitled companies
            whichUser = Utils.idGenerator(user.getCompanyID(), user.getManagerID());
        }
        UserEntitlement userEntitlement = getUserEntitlements(whichUser);
        if (userEntitlement != null && !CollectionUtils.isEmpty(userEntitlement.getEntities())) {
            userEntitlement.getEntities()
                    .forEach(entityID -> entities.add(entityManagementHelper.getEntity(entityID)));
        }
        return entities;
    }

    public int createUserEntitlement(String userID, String entityID) {
        return userManagementDAO.createUserEntitlement(userID, entityID);
    }

    @Transactional
    public boolean deleteUserEntitlement(String userID, String entityID) throws ApplicationException {
        int rows = userManagementDAO.deleteUserEntitlement(userID, entityID);
        if (rows > 1)
            throw new ApplicationException("no of rows deleted is not equal to 1", HttpStatus.BAD_REQUEST);
        return true;
    }

    public List<User> getSubordinates(String incdocsID) {
        return userManagementDAO.getARMforRM(incdocsID);
    }
}
