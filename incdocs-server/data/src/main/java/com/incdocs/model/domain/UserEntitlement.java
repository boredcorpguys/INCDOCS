package com.incdocs.model.domain;

import java.util.ArrayList;
import java.util.List;

public class UserEntitlement {
    private String userID;
    private List<String> entities = new ArrayList<>();

    public String getUserID() {
        return userID;
    }

    public UserEntitlement setUserID(String userID) {
        this.userID = userID;
        return this;
    }

    public List<String> getEntities() {
        return entities;
    }

    public UserEntitlement addEntity(String entityID) {
        this.entities.add(entityID);
        return this;
    }

    public UserEntitlement addEntities(List<String> entities) {
        if (entities != null) {
            this.entities.addAll(entities);
        }
        return this;
    }
}
