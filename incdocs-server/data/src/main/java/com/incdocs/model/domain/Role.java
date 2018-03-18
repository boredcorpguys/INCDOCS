package com.incdocs.model.domain;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Role {
    private final int roleID;
    private String roleName;
    private String description;
    private boolean isClient, isActive;

    public Role(int roleID) {
        this.roleID = roleID;
    }

    public int getRoleID() {
        return roleID;
    }

    public String getRoleName() {
        return roleName;
    }

    public Role setRoleName(String roleName) {
        this.roleName = roleName;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Role setDescription(String description) {
        this.description = description;
        return this;
    }

    public boolean isClient() {
        return isClient;
    }

    public Role setClient(boolean client) {
        isClient = client;
        return this;
    }

    public boolean isActive() {
        return isActive;
    }

    public Role setActive(boolean active) {
        isActive = active;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;

        Role role = (Role) o;

        if (getRoleID() != role.getRoleID()) return false;
        return getRoleName().equals(role.getRoleName());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(roleID).toHashCode();
    }
}
