package com.incdocs.utils;

import com.incdocs.model.constants.ApplicationConstants.Action;
import com.incdocs.model.constants.ApplicationConstants.Role;
import com.incdocs.model.domain.User;
import com.incdocs.model.response.RoleActions;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.http.HttpStatus;

import java.util.Optional;

public class Utils {
    public static String idGenerator(String companyID, String empID) {
        return empID.toLowerCase() + "_" + companyID.toLowerCase();
    }

    public static <T> void validate(T field, String fieldName) {
        boolean valid = true;
        if (field instanceof String)
            valid = StringUtils.isNotEmpty((String) field);
        else
            valid = (field != null);
        if (!valid)
            throw new IllegalArgumentException(fieldName + " is null");
    }

    public static <T> void validate(T field, String message, HttpStatus statusCode)
            throws ApplicationException {
        try {
            validate(field, "");
        } catch (IllegalArgumentException ie) {
            throw new ApplicationException(message, statusCode);
        }
    }

    public static String getCellValue(Cell cell) {
        return Optional.ofNullable(cell)
                .map(Cell::getStringCellValue)
                .orElse(null);
    }

    public static void validateRoleActions(Action action, RoleActions roleActions) throws ApplicationException {
        boolean valid = roleActions.getActions().stream().anyMatch(a -> action.name().equals(a.getActionName()));
        if (!valid) {
            throw new ApplicationException(String.format("You are not authorized to make this request"),
                    HttpStatus.UNAUTHORIZED);
        }
    }

    public static void validateRole(User user, Role role) throws ApplicationException {
        Role userRole = Role.fromValue(user.getRoleID());
        if (userRole != role) {
            throw new ApplicationException(String.format("You are not authorized to make this request"),
                    HttpStatus.UNAUTHORIZED);
        }
    }
}
