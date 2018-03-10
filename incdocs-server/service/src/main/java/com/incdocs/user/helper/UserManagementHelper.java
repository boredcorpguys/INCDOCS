package com.incdocs.user.helper;

import com.incdocs.entity.helper.EntityManagementHelper;
import com.incdocs.user.dao.UserDAO;
import com.incdocs.utils.ApplicationException;
import com.indocs.cache.CacheName;
import com.indocs.model.domain.User;
import com.indocs.model.request.UserCreateRequest;
import com.indocs.model.request.UserProfileRequest;
import com.indocs.model.response.UserEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.indocs.model.constants.ApplicationConstants.Roles;
import static com.indocs.model.constants.ApplicationConstants.UserStatus.INACTIVE;

@Component("userManagementHelper")
public class UserManagementHelper implements InitializingBean {
    @Autowired
    private org.springframework.cache.CacheManager cacheManager;

    private Cache userEntitementCache;

    @Autowired
    @Qualifier("entityManagementHelper")
    private EntityManagementHelper entityManagementHelper;

    @Autowired
    @Qualifier("userManagementDAO")
    private UserDAO userManagementDAO;


    @Cacheable(value = "userEntitlementCache", key = "#id")
    public UserEntity getUserRolesActions(String id) throws ApplicationException {
        UserEntity userEntity = userManagementDAO.getUserRolesActions(id);
        if (userEntity == null) {
            throw new ApplicationException(String.format("User %s doesnt exist in the system", id),
                    HttpStatus.BAD_REQUEST);
        }
        if (userEntity.getUser().getStatus() == INACTIVE) {
            throw new ApplicationException(String.format("User %s is inactive in the system", id),
                    HttpStatus.BAD_REQUEST);
        }
        return userManagementDAO.getUserRolesActions(id);
    }

    public int modifyUserDetails(UserProfileRequest user) {
        return userManagementDAO.modifyUserDetails(user);
    }

    public User getUser(String id) throws ApplicationException {
        UserEntity userEntity = null;
        if (userEntitementCache.get(id) != null) {
            userEntity = (UserEntity) userEntitementCache.get(id).get();
        } else {
            userEntity = getUserRolesActions(id);
        }
        if (userEntity != null) {
            return userEntity.getUser();
        }
        return null;
    }

    public String createUser(String adminID, UserCreateRequest userCreateRequest) throws ApplicationException {
        // validate if not an existing user
        User user = getUser(userCreateRequest.getId());
        if (user != null) {
            throw new ApplicationException(String.format("user id: %s, is already present in system"),
                    HttpStatus.BAD_REQUEST);
        }

        // validate the role passed is a system defined role
        String roleID = userCreateRequest.getRole();
        if (Roles.valueOf(roleID) == null) {
            throw new ApplicationException(String.format("%s is invalid role", roleID)
                    , HttpStatus.BAD_REQUEST);
        }

        // validate the entity
        String companyID = userCreateRequest.getCompanyID();
        if (Objects.nonNull(entityManagementHelper.getEntity(companyID)))
            throw new ApplicationException("entity not setup in incdocs",
                    HttpStatus.BAD_REQUEST);

        // validate if the requested company belongs to admin
        UserEntity admin = getUserRolesActions(adminID);
        if (!StringUtils.equals(companyID, admin.getUser().getCompanyID())) {
            throw new ApplicationException(String.format("user cant be setup for %id", companyID),
                    HttpStatus.BAD_REQUEST);
        }

        // check if role is group head
        Roles role = Roles.valueOf(roleID);
        if (role != Roles.GROUP_HEAD) {
            User groupHead = getUser(userCreateRequest.getGhID());
            if (groupHead != null) {
                throw new ApplicationException(String.format("group head id: %s, is not configured in system"),
                        HttpStatus.BAD_REQUEST);
            }
        }

        return userManagementDAO.createUser(userCreateRequest);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        userEntitementCache = cacheManager.getCache(CacheName.USER_ENTITLEMENTS.getName());
    }
}
