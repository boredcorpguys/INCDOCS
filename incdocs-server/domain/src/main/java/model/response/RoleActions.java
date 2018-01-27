package model.response;

import model.domain.Action;
import model.domain.Role;

import java.util.*;

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
