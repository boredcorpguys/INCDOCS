package com.incdocs.model.response;

import com.incdocs.model.domain.Action;
import com.incdocs.model.domain.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoleActions {
    private Role role;
    private List<Action> actions = new ArrayList<>();

    public Role getRole() {
        return role;
    }

    public RoleActions setRole(Role role) {
        this.role = role;
        return this;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void addAction(Action action) {
        Optional.ofNullable(action).ifPresent(a -> actions.add(a));
    }
}
