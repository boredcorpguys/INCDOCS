package com.incdocs.user.validator;

import com.incdocs.entitlement.helper.EntitlementManagementHelper;
import com.incdocs.entity.helper.EntityManagementHelper;
import com.incdocs.model.constants.ApplicationConstants.Role;
import com.incdocs.model.domain.Entity;
import com.incdocs.model.domain.User;
import com.incdocs.model.request.CreateCompanyRequest;
import com.incdocs.model.request.CreateUserRequest;
import com.incdocs.user.helper.UserManagementHelper;
import com.incdocs.utils.ApplicationException;
import com.incdocs.utils.Utils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.incdocs.utils.Utils.validate;
import static com.incdocs.utils.Utils.validateRole;

@Aspect
@Component
public class AdminServiceValidator {

    private static final Logger logger = LoggerFactory.getLogger(AdminServiceValidator.class);

    @Autowired
    @Qualifier("userManagementHelper")
    private UserManagementHelper userManagementHelper;

    @Autowired
    @Qualifier("entityManagementHelper")
    private EntityManagementHelper entityManagementHelper;

    @Autowired
    @Qualifier("entitlementHelper")
    private EntitlementManagementHelper entitlementHelper;

    @Before("execution(* com.incdocs.user.services.AdminService.*(..))")
    public void validateAdmin(JoinPoint joinPoint) throws ApplicationException {
        String incdocsID = (String) joinPoint.getArgs()[0];
        User admin = userManagementHelper.getUser(incdocsID);
        validateRole(admin, Role.ADMIN);
    }

    @Before("execution(* com.incdocs.user.services.AdminService.createUser(..))")
    public void validateCreateUserOperation(JoinPoint joinPoint) throws ApplicationException {

        logger.debug("validating create user operation with args");
        String incdocsID = (String) joinPoint.getArgs()[0];
        CreateUserRequest userCreateRequest = (CreateUserRequest) joinPoint.getArgs()[1];

        validate(userCreateRequest.getName(), "name");
        validate(userCreateRequest.getId(), "id");

        // validate the role passed is a system defined role
        User admin = userManagementHelper.getUser(incdocsID);
        List<com.incdocs.model.domain.Role> entityRoles = entityManagementHelper.getEntityRoles(admin.getCompanyID());
        com.incdocs.model.domain.Role role = entityRoles.stream()
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
        if (Role.valueOf(role.getRoleName()) != Role.GROUP_HEAD) {
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
        logger.debug("validating create company operation with args");
        CreateCompanyRequest createCompanyRequest = (CreateCompanyRequest) joinPoint.getArgs()[1];
        validate(createCompanyRequest.getId(), "id");
        validate(createCompanyRequest.getName(), "name");
        validate(createCompanyRequest.getPan(), "pan");
        validate(createCompanyRequest.getGhID(), "group head id");
        Entity entity = entityManagementHelper.getEntity(createCompanyRequest.getId());
        if (entity != null) {
            throw new ApplicationException(String.format("Company: %s is already setup in system",
                    createCompanyRequest.getName()),
                    HttpStatus.CONFLICT);
        }
        User groupHead = userManagementHelper.getUserByEmpID(createCompanyRequest.getGhID());
        if (groupHead == null) {
            throw new ApplicationException(String.format("group head id: %s, is not configured in system",
                    createCompanyRequest.getGhID()),
                    HttpStatus.BAD_REQUEST);
        }

    }
}
