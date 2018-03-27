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

    public enum Roles {
        ADMIN(null),
        GROUP_HEAD(1),
        RM(2),
        ARM(3),
        CMO(null),
        REQUESTOR(null),
        APPROVER(null),
        AUTH_SIGNATORY(null),
        CS(null);

        private Integer priority = Integer.MAX_VALUE;

        Roles(Integer priority) {
            if (priority != null)
                this.priority = priority;
        }

        public Integer getPriority() {
            return priority;
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
}
