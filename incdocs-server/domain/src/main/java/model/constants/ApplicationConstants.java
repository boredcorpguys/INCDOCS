package model.constants;

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
}
