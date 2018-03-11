package com.incdocs.user.validator;

import com.incdocs.entity.helper.EntityManagementHelper;
import com.incdocs.user.helper.UserManagementHelper;
import com.incdocs.utils.ApplicationException;
import com.indocs.model.constants.ApplicationConstants;
import com.indocs.model.domain.Role;
import com.indocs.model.domain.User;
import com.indocs.model.request.CreateCompanyRequest;
import com.indocs.model.request.CreateUserRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.incdocs.utils.Utils.validate;

@Aspect
@Component
public class AdminServiceValidator {

    @Autowired
    @Qualifier("userManagementHelper")
    private UserManagementHelper userManagementHelper;

    @Autowired
    @Qualifier("entityManagementHelper")
    private EntityManagementHelper entityManagementHelper;

    @Before("execution(* com.incdocs.user.services.AdminService.createUser (java.lang.String, com.indocs.model.request.CreateUserRequest))")
    public void validateCreateUserOperation(JoinPoint joinPoint) throws ApplicationException {

        System.out.println("validating create user operation with args");
        String incdocsID = (String) joinPoint.getArgs()[0];
        CreateUserRequest userCreateRequest = (CreateUserRequest) joinPoint.getArgs()[1];

        validate(userCreateRequest.getName(), "name");
        validate(userCreateRequest.getId(), "id");

        // validate the role passed is a system defined role
        User admin = userManagementHelper.getUser(incdocsID);
        List<Role> entityRoles = entityManagementHelper.getEntityRoles(admin.getCompanyID());
        Role role = entityRoles.stream()
                .filter(r -> r.getRoleID() == userCreateRequest.getRoleID())
                .findFirst().orElse(null);
        validate(role, String.format("%s is invalid role", userCreateRequest.getRoleID())
                , HttpStatus.BAD_REQUEST);

        // check if role is group head
        if (ApplicationConstants.Roles.valueOf(role.getRoleName()) != ApplicationConstants.Roles.GROUP_HEAD) {
            validate(userCreateRequest.getGhID(), "ghID");
            User groupHead = userManagementHelper.getUser(userCreateRequest.getGhID());
            validate(groupHead,
                    String.format("group head id: %s, is not configured in system",userCreateRequest.getGhID()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @Before("execution(* com.incdocs.user.services.AdminService.createCompany (java.lang.String, com.indocs.model.request.CreateCompanyRequest))")
    public void validateCreateCompanyRequest(JoinPoint joinPoint) throws ApplicationException {
        System.out.println("validating create company operation with args");
        CreateCompanyRequest createCompanyRequest = (CreateCompanyRequest)joinPoint.getArgs()[1];
        validate(createCompanyRequest.getId(),"id");
        validate(createCompanyRequest.getName(), "name");
        validate(createCompanyRequest.getPan(), "pan");
    }
}
