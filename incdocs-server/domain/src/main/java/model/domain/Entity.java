package model.domain;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Entity {
    private int entityID;
    private int parentID;
    private String name;

    public int getEntityID() {
        return entityID;
    }

    public int getParentID() {
        return parentID;
    }

    public String getName() {
        return name;
    }

    public Entity setEntityID(int entityID) {
        this.entityID = entityID;
        return this;
    }

    public Entity setParentID(int parentID) {
        this.parentID = parentID;
        return this;
    }

    public Entity setName(String name) {
        this.name = name;
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
