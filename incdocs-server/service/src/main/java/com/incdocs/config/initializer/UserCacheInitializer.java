package com.incdocs.config.initializer;

import com.incdocs.cache.AppCacheManager;
import com.incdocs.cache.CacheName;
import com.incdocs.model.domain.User;
import com.incdocs.user.dao.UserDAO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("userCacheInitializer")
public class UserCacheInitializer implements InitializingBean{

    @Autowired
    private AppCacheManager appCacheManager;

    @Autowired
    private UserDAO userDAO;

    @Override
    public void afterPropertiesSet() throws Exception {
        List<User> allUsers = userDAO.getAllUsers();
        if (CollectionUtils.isNotEmpty(allUsers)) {
            allUsers.forEach(user -> appCacheManager.put(CacheName.USER, user.getIncdocsID(), user));
        }
    }
}
