package model.response;

import model.domain.Action;
import model.domain.Entity;
import model.domain.Role;
import model.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRoleActionsOnEntity
{
    private final User user;
    private RoleActions roleActions;
    private final List<Entity> entities;

    public UserRoleActionsOnEntity(User user) {
        this.user = user;
        this.entities = new ArrayList<>();
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public User getUser() {
        return user;
    }

    public RoleActions getRoleActions() {
        return roleActions;
    }

    public UserRoleActionsOnEntity setRoleActions(RoleActions roleActions) {
        this.roleActions = roleActions;
        return this;
    }

    public void add(Entity e) {
        Optional.of(e).ifPresent(entity -> entities.add(entity));
    }
}
