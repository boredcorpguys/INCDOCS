package com.incdocs.user.helper;

import com.incdocs.entitlement.helper.EntitlementHelper;
import com.incdocs.model.domain.Role;
import com.incdocs.model.domain.User;
import com.incdocs.model.request.CreateUserRequest;
import com.incdocs.user.services.AdminService;
import com.incdocs.utils.ApplicationException;
import com.incdocs.model.constants.ApplicationConstants;
import com.incdocs.model.domain.BulkUploadRow;
import com.incdocs.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component("bulkUploadMappingProcessor")
public class BulkUploadMappingProcessor {

    @Autowired
    private AdminService adminService;

    @Autowired
    @Qualifier("entitlementHelper")
    private EntitlementHelper entitlementHelper;

    @Autowired
    private UserManagementHelper userManagementHelper;

    public List<BulkUploadRow> processRows(String adminID, List<BulkUploadRow> rows) throws ApplicationException {
        if (rows.isEmpty()) {
            throw new ApplicationException("Empty excel", HttpStatus.BAD_REQUEST);
        }

        List<String> tags = Stream.of(ApplicationConstants.BulkUploadRowTags.values())
                .map(constant -> constant.name())
                .collect(Collectors.toList());

        BulkUploadRow header = rows.remove(0);
        boolean headerVerified = tags.stream()
                .allMatch(tag -> tag.equals(header.getName())
                        || tag.equals(header.getEmpID())
                        || tag.equals(header.getGhID())
                        || tag.equals(header.getRole()));
        if (!headerVerified) {
            throw new ApplicationException("Excel headers should be: " + tags, HttpStatus.BAD_REQUEST);
        }

        if (rows.isEmpty()) {
            throw new ApplicationException("Excel has no mapping data", HttpStatus.BAD_REQUEST);
        }

        List<BulkUploadRow> errorRows = new ArrayList<>();

        Role ghRole = entitlementHelper.getRole(ApplicationConstants.Roles.GROUP_HEAD);

        List<BulkUploadRow> ghRows = new ArrayList<>();
        List<BulkUploadRow> nonGhRows = new ArrayList<>();

        rows.stream()
                .forEach(row -> {
                    if (checkGHPredicate(ghRole).test(row)) {
                        ghRows.add(row);
                    } else {
                        nonGhRows.add(row);
                    }
                });
        User admin = userManagementHelper.getUser(adminID);
        ghRows.forEach(createUserConsumer(admin, errorRows, ghRole));
        nonGhRows.forEach(row -> {
            Role currRole = entitlementHelper.getRole(ApplicationConstants.Roles.valueOf(row.getRole()));
            createUserConsumer(admin, errorRows, currRole).accept(row);
        });

        return errorRows;
    }

    private Predicate<BulkUploadRow> checkGHPredicate(Role ghRole) {
        return row -> {
            try {
                Role currRole = entitlementHelper.getRole(ApplicationConstants.Roles.valueOf(row.getRole()));
                return currRole.getRoleID() == ghRole.getRoleID();
            } catch (Exception e) {
                return false;
            }
        };
    }

    private Consumer<BulkUploadRow> createUserConsumer(User admin, List<BulkUploadRow> errorRows, Role role) {
        return rowToInsert -> {
            CreateUserRequest createUserRequest = new CreateUserRequest()
                    .setRoleID(role.getRoleID())
                    .setId(rowToInsert.getEmpID())
                    .setGhID(rowToInsert.getGhID())
                    .setName(rowToInsert.getName());
            try {
                adminService.createUser(admin.getIncdocsID(), createUserRequest);
            } catch (ApplicationException e) {
               if (e.getHttpStatusCode() == HttpStatus.BAD_REQUEST) {
                   rowToInsert.setErrorStatus(e.getMessage());
                   errorRows.add(rowToInsert);
               }
            }
        };
    }
}
