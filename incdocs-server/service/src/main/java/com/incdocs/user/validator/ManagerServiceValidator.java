package com.incdocs.user.validator;

import com.incdocs.entitlement.helper.EntitlementManagementHelper;
import com.incdocs.entity.helper.EntityManagementHelper;
import com.incdocs.model.domain.Entity;
import com.incdocs.model.domain.User;
import com.incdocs.model.domain.UserEntitlement;
import com.incdocs.model.response.RoleActions;
import com.incdocs.user.helper.UserManagementHelper;
import com.incdocs.utils.ApplicationException;
import com.incdocs.utils.Utils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static com.incdocs.model.constants.ApplicationConstants.Action.VIEW_SUBORDINATES;
import static com.incdocs.model.constants.ApplicationConstants.Role;
import static com.incdocs.utils.Utils.*;


@Aspect
@Component
public class ManagerServiceValidator {

    @Autowired
    private UserManagementHelper userManagementHelper;

    @Autowired
    private EntitlementManagementHelper entitlementHelper;

    @Autowired
    private EntityManagementHelper entityManagementHelper;

    @Before("execution(* com.incdocs.user.services.ManagerActionService.*(..))")
    public void validateManager(JoinPoint joinPoint) throws ApplicationException {
        String incdocsID = (String) joinPoint.getArgs()[0];
        User manager = userManagementHelper.getUser(incdocsID);
        validateRole(manager, Role.RM);
    }

    public void validateModifyMemberMapping(String incdocsID, String memberID, String entityID)
            throws ApplicationException {
        validate(memberID, "memberId");
        validate(entityID, "entityId");
        User manager = userManagementHelper.getUser(incdocsID);
        com.incdocs.model.domain.Role role = entitlementHelper.getRole(manager.getRoleID());
        List<Role> validRoles = Arrays.asList(Role.GROUP_HEAD, Role.RM);
        if (!validRoles.contains(Role.valueOf(role.getRoleName()))) {
            throw new ApplicationException(String.format("%s is not authorized to make this request", incdocsID),
                    HttpStatus.UNAUTHORIZED);
        }
        String memberIncdocsID = Utils.idGenerator(manager.getCompanyID(), memberID);
        User member = userManagementHelper.getUser(memberIncdocsID);
        if (member == null) {
            throw new ApplicationException(String.format("%s is not a valid member", memberID),
                    HttpStatus.BAD_REQUEST);
        }

        Entity entity = entityManagementHelper.getEntity(entityID);
        if (entity == null) {
            throw new ApplicationException(String.format("%s is not a valid company", entityID),
                    HttpStatus.BAD_REQUEST);
        }
        UserEntitlement managerEntitlement = userManagementHelper.getUserEntitlements(incdocsID);
        List<String> managerEntities = managerEntitlement.getEntities();
        if (managerEntities.contains(entityID)) {
            throw new ApplicationException(String.format("%s is not authorized to make this request", incdocsID),
                    HttpStatus.UNAUTHORIZED);
        }
    }

    @Before("execution(* com.incdocs.user.services.ManagerActionService.deleteMemberToCompanyMapping(..))")
    public void validateDeleteMemberToCompanyMapping(JoinPoint joinPoint) throws ApplicationException {
        String incdocsID = (String) joinPoint.getArgs()[0];
        String memberID = (String) joinPoint.getArgs()[1];
        String entityID = (String) joinPoint.getArgs()[2];
        validateModifyMemberMapping(incdocsID, memberID, entityID);
        User manager = userManagementHelper.getUser(incdocsID);
        String memberIncdocsID = Utils.idGenerator(manager.getCompanyID(), memberID);
        UserEntitlement memberEntitlement = userManagementHelper.getUserEntitlements(memberIncdocsID);
        List<String> memberEntities = memberEntitlement.getEntities();
        if (!memberEntities.contains(entityID)) {
            throw new ApplicationException(String.format("%s is not mapped to company %s", memberID, entityID),
                    HttpStatus.BAD_REQUEST);
        }

    }

    public void validateAddMemberToCompanyMapping(JoinPoint joinPoint) throws ApplicationException {
        String incdocsID = (String) joinPoint.getArgs()[0];
        String memberID = (String) joinPoint.getArgs()[1];
        String entityID = (String) joinPoint.getArgs()[2];
        validateModifyMemberMapping(incdocsID, memberID, entityID);
        User manager = userManagementHelper.getUser(incdocsID);
        User member = userManagementHelper.getUserByEmpID(memberID);
        String ghOfManager = manager.getManagerID();
        String ghOfMember = member.getManagerID();
        if (!StringUtils.equals(ghOfManager, ghOfMember)) {
            throw new ApplicationException(String.format("group head of member %s is different from your group head",
                    memberID),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @Before("execution(* com.incdocs.user.services.ManagerActionService.searchSubordindates(..))")
    public void validateSearchSubordindates(JoinPoint joinPoint) throws ApplicationException {
        String incdocsID = (String) joinPoint.getArgs()[0];
        User rm = userManagementHelper.getUser(incdocsID);
        RoleActions roleActions = entitlementHelper.getActionsForRole(rm.getRoleID());
        validateRoleActions(VIEW_SUBORDINATES, roleActions);
    }
}
