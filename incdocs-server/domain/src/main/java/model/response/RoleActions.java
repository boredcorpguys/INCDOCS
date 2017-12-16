package model.response;

import model.domain.Action;
import model.domain.Role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoleActions {
    private Map<Role,List<Action>> rolesByAction = new HashMap<>();

    public void addActionForRole(Role role, Action action) {
        if(role==null)
            throw new IllegalArgumentException("role cant be null");

        List<Action> actions = rolesByAction.get(role);
        if(actions==null) {
            actions = new ArrayList<Action>();
            rolesByAction.put(role,actions);
        }
        actions.add(action);
    }

    public Map<Role, List<Action>> getRolesByAction() {
        return rolesByAction;
    }
}
