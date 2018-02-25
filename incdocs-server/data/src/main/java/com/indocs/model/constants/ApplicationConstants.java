package com.indocs.model.constants;

import java.util.Arrays;

public class ApplicationConstants {
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
        ADMIN,
        GROUP_HEAD,
        RM,
        ARM,
        CMO,
        REQUESTOR,
        APPROVER,
        AUTH_SIGNATORY,
        CS;
    }

    public enum UserStatus {
        ACTIVE("A"),
        INACTIVE("I"),
        NEW("N");

        private String status;

        UserStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }
        public static UserStatus fromStatus(String status) {
            return Arrays.stream(UserStatus.values())
                    .filter(info -> info.status.equals(status))
                    .findFirst().orElse(null);
        }
    }
}
