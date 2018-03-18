package com.incdocs.model.response;

import com.incdocs.model.domain.Entity;
import com.incdocs.model.domain.User;

import java.util.ArrayList;
import java.util.List;

public class UserEntity {
    private final User user;
    private final List<String> entities;

    public UserEntity(User user) {
        this.user = user;
        this.entities = new ArrayList<>();
    }

    public List<String> getEntities() {
        return new ArrayList<>(entities);
    }

    public User getUser() {
        return user;
    }

    public UserEntity addEntity(Entity entity) {
        return this.addEntity(entity.getEntityID());
    }

    public UserEntity addEntity(String entityID) {
        entities.add(entityID);
        return this;
    }
}
