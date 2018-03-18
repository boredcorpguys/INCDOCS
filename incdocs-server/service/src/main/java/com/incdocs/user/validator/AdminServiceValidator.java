package com.incdocs.user.validator;

import com.incdocs.entitlement.helper.EntitlementHelper;
import com.incdocs.entity.helper.EntityManagementHelper;
import com.incdocs.user.helper.UserManagementHelper;
import com.incdocs.utils.ApplicationException;
import com.incdocs.utils.Utils;
import com.incdocs.model.constants.ApplicationConstants;
import com.incdocs.model.domain.Entity;
import com.incdocs.model.domain.Role;
import com.incdocs.model.domain.User;
import com.incdocs.model.request.CreateCompanyRequest;
import com.incdocs.model.request.CreateUserRequest;
import com.incdocs.model.response.RoleActions;
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

    @Autowired
    @Qualifier("entitlementHelper")
    private EntitlementHelper entitlementHelper;

    @Before("execution(* com.incdocs.user.services.AdminService.*(..))")
    public void validateAdmin(JoinPoint joinPoint) throws ApplicationException {
        String incdocsID = (String) joinPoint.getArgs()[0];
        User admin = userManagementHelper.getUser(incdocsID);
        RoleActions roleActions = entitlementHelper.getActionsForRole(admin.getRoleID());
        if (roleActions == null || !roleActions.getRole().getRoleName()
                .equals(ApplicationConstants.Roles.ADMIN.name())) {
            throw new ApplicationException(String.format("%s is not authorized to make this request",incdocsID),
                    HttpStatus.UNAUTHORIZED);
        }
    }

    @Before("execution(* com.incdocs.user.services.AdminService.createUser(..))")
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

        // validate if user is already present in system
        User user = userManagementHelper.getUser(Utils.idGenerator(admin.getCompanyID(),
                userCreateRequest.getId()));
        if (user != null) {
            throw new ApplicationException(String.format("userID: %s is already setup in the system",
                    userCreateRequest.getId()), HttpStatus.CONFLICT);
        }

        // check if role is group head
        if (ApplicationConstants.Roles.valueOf(role.getRoleName()) != ApplicationConstants.Roles.GROUP_HEAD) {
            validate(userCreateRequest.getGhID(), "ghID");
            String ghIncdocsID = Utils.idGenerator(admin.getCompanyID(), userCreateRequest.getGhID());
            User groupHead = userManagementHelper.getUser(ghIncdocsID);
            validate(groupHead,
                    String.format("group head id: %s, is not configured in system", userCreateRequest.getGhID()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @Before("execution(* com.incdocs.user.services.AdminService.createCompany(..))")
    public void validateCreateCompanyRequest(JoinPoint joinPoint) throws ApplicationException {
        System.out.println("validating create company operation with args");
        CreateCompanyRequest createCompanyRequest = (CreateCompanyRequest) joinPoint.getArgs()[1];
        validate(createCompanyRequest.getId(), "id");
        validate(createCompanyRequest.getName(), "name");
        validate(createCompanyRequest.getPan(), "pan");
        Entity entity = entityManagementHelper.getEntity(createCompanyRequest.getId());
        if (entity != null) {
            throw new ApplicationException(String.format("Company: %s is already setup in system",
                    createCompanyRequest.getName()),
                    HttpStatus.CONFLICT);
        }
    }
}