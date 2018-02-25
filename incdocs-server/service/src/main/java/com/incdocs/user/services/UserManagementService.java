package com.incdocs.user.services;

import com.incdocs.user.helper.UserManagementHelper;
import com.indocs.model.domain.User;
import com.indocs.model.request.UserProfileRequest;
import com.indocs.model.response.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("incdocs/user")
public class UserManagementService {
    @Autowired
    @Qualifier("userManagementHelper")
    private UserManagementHelper userManagementHelper;

    /**
     * gets the user details along with the entities he belongs to
     *
     * @param user
     * @return
     */
    @PostMapping("/login")
    public UserEntity login(@RequestBody UserProfileRequest user) {
        return userManagementHelper.getUserRolesActions(user.getId());
    }

    /**
     * /profile services doesnt contain information related to users entitled companies
     *
     * @param user
     * @return
     */
    @PostMapping("/profile/modify")
    public int modifyUserDetails(@RequestBody UserProfileRequest user) {
        return userManagementHelper.modifyUserDetails(user);
    }

    /**
     * gets the user profile details
     *
     * @param id
     * @return
     */
    @GetMapping("/profile")
    public User getUserDetails(@RequestParam(value = "id", required = true) String id) {
        return userManagementHelper.getUserDetails(id);
    }
}
