package com.incdocs.user.helper;

import com.incdocs.cache.AppCacheManager;
import com.incdocs.entity.helper.EntityManagementHelper;
import com.incdocs.user.dao.UserDAO;
import com.incdocs.utils.ApplicationException;
import com.incdocs.cache.CacheName;
import com.incdocs.model.domain.User;
import com.incdocs.model.request.CreateUserRequest;
import com.incdocs.model.request.UserProfileRequest;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

@Component("userManagementHelper")
public class UserManagementHelper {
    @Autowired
    private AppCacheManager appCacheManager;

    @Autowired
    @Qualifier("entityManagementHelper")
    private EntityManagementHelper entityManagementHelper;

    @Autowired
    @Qualifier("userManagementDAO")
    private UserDAO userManagementDAO;

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

    public String createUser(String adminID, CreateUserRequest createUserRequest) throws ApplicationException {
        User admin = getUser(adminID);
        createUserRequest.setCompanyID(admin.getCompanyID());
        createUserRequest.setClient(admin.isClient());
        String incdocsID = userManagementDAO.createUser(createUserRequest);
        getUser(incdocsID); //cache it
        return incdocsID;
    }
}
