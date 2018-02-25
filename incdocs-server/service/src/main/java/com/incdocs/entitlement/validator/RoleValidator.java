package com.incdocs.entitlement.validator;

import com.incdocs.utils.ApplicationException;
import com.incdocs.utils.Validator;
import com.indocs.model.constants.ApplicationConstants;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import static com.indocs.model.constants.ApplicationConstants.Roles;
@Component("roleValidator")
public class RoleValidator implements Validator<String> {
    public void validate(String roleID) throws ApplicationException {
        if(Roles.valueOf(roleID) == null) {
            throw new ApplicationException(String.format("%s is invalid role",roleID)
            , HttpStatus.BAD_REQUEST);
        }
    }
}
