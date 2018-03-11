package com.incdocs.user.helper;

import com.incdocs.entity.helper.EntityManagementHelper;
import com.incdocs.user.dao.UserDAO;
import com.incdocs.utils.ApplicationException;
import com.indocs.cache.CacheName;
import com.indocs.model.domain.User;
import com.indocs.model.request.CreateCompanyRequest;
import com.indocs.model.request.CreateUserRequest;
import com.indocs.model.request.UserProfileRequest;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

@Component("userManagementHelper")
public class UserManagementHelper implements InitializingBean {
    @Autowired
    private org.springframework.cache.CacheManager cacheManager;

    private Cache userCache;

    @Autowired
    @Qualifier("entityManagementHelper")
    private EntityManagementHelper entityManagementHelper;

    @Autowired
    @Qualifier("userManagementDAO")
    private UserDAO userManagementDAO;

    public int modifyUserDetails(UserProfileRequest user) {
        return userManagementDAO.modifyUserDetails(user);
    }

    @Cacheable(value = "userCache", key = "#incdocsID")
    public User getUser(String incdocsID) {
        User user = null;
        if (userCache.get(incdocsID) != null) {
            user = (User) userCache.get(incdocsID).get();
        } else {
            try{
                user = userManagementDAO.getUser(incdocsID);
            }
            catch (EmptyResultDataAccessException e) {
                System.out.println(e.getMessage());
            }
        }
        return user;
    }

    public String createUser(String adminID, CreateUserRequest createUserRequest) throws ApplicationException {
        User admin = getUser(adminID);
        createUserRequest.setCompanyID(admin.getCompanyID());
        createUserRequest.setClient(admin.isClient());
        return userManagementDAO.createUser(createUserRequest);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        userCache = cacheManager.getCache(CacheName.USER.getName());
    }
}
