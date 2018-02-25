package com.indocs.cache;

import java.util.Arrays;

public enum CacheName {
    USER("userCache"),
    USER_ENTITLEMENTS("userEntitlementCache"),
    ENTITY("entityCache"),
    ENTITY_ROLES("entityRoleCache");

    private String name;

    CacheName(String name) {
        this.name = name;
    }

    public static CacheName fromName(String name) {
        return Arrays.stream(CacheName.values())
                .filter(cacheName -> cacheName.equals(name))
                .findFirst()
                .orElse(null);
    }

    public String getName() {
        return name;
    }
}
