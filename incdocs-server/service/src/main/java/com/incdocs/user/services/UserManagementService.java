package com.incdocs.user.services;

import com.incdocs.user.dao.UserDAO;
import model.domain.User;
import model.request.InputUser;
import model.response.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("incdocs/user")
@CacheConfig(cacheNames = "userEntitlements")
public class UserManagementService {
    @Autowired
    @Qualifier("userManagementDAO")
    private UserDAO userManagementDAO;

    @PostMapping("/login")
    @Cacheable
    public UserEntity login(@RequestBody InputUser user) {
        return userManagementDAO.getUserRolesActions(user.getId());
    }

    @PostMapping("/profile/modify")
    @Cacheable
    public int modifyUserDetails(@RequestBody InputUser user) {
        return userManagementDAO.modifyUserDetails(user);
    }

    @GetMapping("/profile")
    public User getUserDetails(@RequestParam(value = "id", required = true) String id) {
        return userManagementDAO.getUser(id);
    }
}
