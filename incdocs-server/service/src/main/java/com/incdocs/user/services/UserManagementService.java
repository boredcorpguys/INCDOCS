package com.incdocs.user.services;

import com.incdocs.user.helper.UserManagementHelper;
import com.incdocs.utils.ApplicationException;
import com.indocs.model.domain.User;
import com.indocs.model.request.UserProfileRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/incdocs/user")
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
    @GetMapping("/login")
    public User login(@RequestHeader(value = "incdocsID") String id) throws ApplicationException {
        return userManagementHelper.getUser(id);
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
}
