package com.incdocs.model.constants;

import java.util.Arrays;

public class ApplicationConstants {

    public enum RequestHeaders {
        ID("incdocsID"),
        PSWD("password"),
        ORIGIN("Access-Control-Allow-Origin");
        private String name;

        RequestHeaders(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public enum EntitlementInfo {
        ROLE("role"),
        ACTION("action"),
        ENTITY("entity");

        private String name;

        EntitlementInfo(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public EntitlementInfo fromName(String name) {
            return Arrays.stream(EntitlementInfo.values())
                    .filter(info -> info.name.equals(name))
                    .findFirst().orElse(null);
        }
    }

    public enum Role {
        ADMIN(1),
        GROUP_HEAD(2),
        RM(3),
        ARM(4),
        CMO(5),
        REQUESTOR(6),
        APPROVER(7),
        AUTH_SIGNATORY(8),
        CS(9);

        private Integer id = Integer.MAX_VALUE;

        Role(Integer id) {
            if (id != null)
                this.id = id;
        }

        public Integer getId() {
            return id;
        }

        public static Role fromValue(int id) {
            return Arrays.stream(values()).filter(role -> role.id == id).findFirst().orElse(null);
        }
    }

    public enum UserStatus {
        ACTIVE("A"),
        INACTIVE("I"),
        NEW("N");

        private String status;

        UserStatus(String status) {
            this.status = status;
        }

        public static UserStatus fromStatus(String status) {
            return Arrays.stream(UserStatus.values())
                    .filter(info -> info.status.equals(status))
                    .findFirst().orElse(null);
        }

        public String getStatus() {
            return status;
        }
    }

    public enum BulkUploadRowTags {
        EmployeeID,
        Role,
        GroupHeadID,
        Name
    }

    public enum BulkUploadCompanyRowTags {
        Name,
        CompanyID,
        Pan
    }

    public enum Action {
        CREATE_USER,
        CREATE_COMPANY,
        BULK_UPLOAD_MAPPINGS,
        VIEW_ANALYTICS,
        VIEW_STOCK_STATEMENT,
        APPROVE_STOCK_STATEMENT,
        REJECT_STOCK_STATEMENT,
        APPROVE_RM_REQUEST,
        VIEW_PORTFOLIO,
        PROMOTE_USER,
        MANAGE_PORTFOLIO,
        REQUEST_COMPANY,
        CHANGE_GH,
        VIEW_ROLES,
        VIEW_SUBORDINATES;
    }
}
