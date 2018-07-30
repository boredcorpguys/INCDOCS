package com.incdocs.user.services;

import com.incdocs.model.domain.Entity;
import com.incdocs.model.domain.User;
import com.incdocs.model.request.UserProfileRequest;
import com.incdocs.user.helper.UserManagementHelper;
import com.incdocs.utils.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.incdocs.model.constants.ApplicationConstants.UserStatus.INACTIVE;

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
        User user = userManagementHelper.getUser(id);
        if (user == null) {
            throw new ApplicationException(String.format("User %s doesnt exist in the system", id),
                    HttpStatus.BAD_REQUEST);
        }
        if (user.getStatus() == INACTIVE) {
            throw new ApplicationException(String.format("User %s is inactive in the system", id),
                    HttpStatus.BAD_REQUEST);
        }
        return user;
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

    @GetMapping("/companies")
    public @ResponseBody
    List<Entity> getEntitledEntities(@RequestHeader(value = "incdocsID") String incdocsID,
                                     @RequestParam(value = "self", required = false) boolean selfEntitled) {
        return userManagementHelper.getEntitledEntities(incdocsID, selfEntitled);
    }
}
