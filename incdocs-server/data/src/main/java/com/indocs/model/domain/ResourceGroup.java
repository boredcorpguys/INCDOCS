package com.indocs.model.domain;

import java.util.ArrayList;
import java.util.List;

public class ResourceGroup {
    private final int id;
    private final List<Entity> entities;

    public ResourceGroup(int id) {
        this.id = id;
        this.entities = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void addEntity(Entity entity) {
        this.entities.add(entity);
    }
}
