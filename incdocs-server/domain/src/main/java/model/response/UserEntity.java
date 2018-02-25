package model.response;

import model.domain.Entity;
import model.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserEntity
{
    private final User user;
    private final String entityID;

    public UserEntity(User user, String entityID) {
        this.user = user;
        this.entityID = entityID;
    }

    public String getEntityID() {
        return entityID;
    }

    public User getUser() {
        return user;
    }
}
