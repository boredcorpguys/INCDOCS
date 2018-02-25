package com.incdocs.user.helper;

import com.incdocs.entitlement.validator.RoleValidator;
import com.incdocs.entity.helper.EntityManagementHelper;
import com.incdocs.entity.validator.EntityValidator;
import com.incdocs.user.dao.UserDAO;
import com.incdocs.user.validator.UserValidator;
import com.incdocs.utils.ApplicationException;
import com.indocs.cache.CacheName;
import com.indocs.model.domain.User;
import com.indocs.model.request.UserCreateRequest;
import com.indocs.model.request.UserProfileRequest;
import com.indocs.model.response.UserEntity;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import static com.indocs.model.constants.ApplicationConstants.Roles;
import java.util.Optional;

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

    @Autowired
    @Qualifier("userValidator")
    private UserValidator userValidator;

    @Autowired
    @Qualifier("roleValidator")
    private RoleValidator roleValidator;

    @Autowired
    @Qualifier("entityValidator")
    private EntityValidator entityValidator;

    @Cacheable(value = "userEntitlementCache", key = "#id")
    public UserEntity getUserRolesActions(String id) {
        return userManagementDAO.getUserRolesActions(id);
    }

    public int modifyUserDetails(UserProfileRequest user) {
        return userManagementDAO.modifyUserDetails(user);
    }

    public User getUserDetails(String id) {
        UserEntity userEntity = Optional.ofNullable(((UserEntity) userEntitementCache.get(id).get()))
                .orElseGet(() -> getUserRolesActions(id));
        if (userEntity != null) {
            return userEntity.getUser();
        }
        return null;
    }

    public String createUser(UserCreateRequest userCreateRequest) throws ApplicationException {
        // validate if not an existing user
        User user = getUserDetails(userCreateRequest.getId());
        if (user != null) {
            throw new ApplicationException(String.format("user id: %s, is already present in system"),
                    HttpStatus.BAD_REQUEST);
        }

        // validate the role passed is a system defined role
        String roleID = userCreateRequest.getRole();
        roleValidator.validate(roleID);

        // validate the entity
        String companyID = userCreateRequest.getCompanyID();
        entityValidator.validate(entityManagementHelper.getEntity(companyID));

        // check if role is group head
        Roles role = Roles.valueOf(roleID);
        if (role != Roles.GROUP_HEAD) {
            User groupHead = getUserDetails(userCreateRequest.getGhID());
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
