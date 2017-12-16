package model.response;

import model.domain.Action;
import model.domain.Entity;
import model.domain.Role;
import model.domain.User;

public class UserActionOnEntity
{
    private User user;
    private Role role;
    private Action[] actions;
    private Entity[] entities;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Action[] getActions() {
        return actions;
    }

    public void setActions(Action[] actions) {
        this.actions = actions;
    }

    public Entity[] getEntities() {
        return entities;
    }

    public void setEntities(Entity[] entities) {
        this.entities = entities;
    }

    public Role getRole() {
        return role;
    }

    public UserActionOnEntity setRole(Role role) {
        this.role = role;
        return this;
    }
}
