package model.domain;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;

public class Entity {
    private final String entityID;

    private String name;

    private String parentID;

    private List<Entity> childEntities = new ArrayList<>();

    public Entity(String entityID) {
        this.entityID = entityID;
    }

    public String getEntityID() {
        return entityID;
    }

    public String getName() {
        return name;
    }

    public List<Entity> getChildEntities() {
        return childEntities;
    }

    public String getParentID() {
        return parentID;
    }

    public Entity setParentID(String parentID) {
        this.parentID = parentID;
        return this;
    }

    public Entity setName(String name) {
        this.name = name;
        return this;
    }

    public Entity addChildEntity(Entity childEntity) {
        this.childEntities.add(childEntity);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entity)) return false;

        Entity entity = (Entity) o;

        if (getEntityID() != entity.getEntityID()) return false;
        return getName().equals(entity.getName());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.entityID).toHashCode();
    }
}
