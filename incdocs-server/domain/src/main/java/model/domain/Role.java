package model.domain;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Role {
    private int roleID;
    private String roleName;
    private String description;

    public int getRoleID() {
        return roleID;
    }
    public String getRoleName() {
        return roleName;
    }
    public String getDescription() {
        return description;
    }

    public Role setRoleID(int roleID) {
        this.roleID = roleID;
        return this;
    }

    public Role setRoleName(String roleName) {
        this.roleName = roleName;
        return this;
    }

    public Role setDescription(String description) {
        this.description = description;
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
