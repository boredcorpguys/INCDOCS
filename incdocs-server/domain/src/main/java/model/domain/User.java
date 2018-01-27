package model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class User {
    private String empID;
    private final String emailID;
    private String name;
    private Integer roleID;
    private String password;

    public User(String emailID) {
        this.emailID = emailID;
    }

    @JsonIgnore
    public String getEmpID() {
        return empID;
    }

    public User setEmpID(String empID) {
        this.empID = empID;
        return this;
    }

    @JsonIgnore
    public String getEmailID() {
        return emailID;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    @JsonIgnore
    public Integer getRoleID() {
        return roleID;
    }

    public User setRoleID(Integer roleID) {
        this.roleID = roleID;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        return getEmailID() != null ? getEmailID().equals(user.getEmailID()) : user.getEmailID() == null;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public int hashCode() {
        return getEmailID() != null ? getEmailID().hashCode() : 0;
    }
}
