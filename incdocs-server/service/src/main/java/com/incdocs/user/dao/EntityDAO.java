package com.incdocs.user.dao;

import model.domain.Entity;
import model.domain.ResourceGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.incdocs.user.dao.QueryManager.Sql.SEL_ENTITIES_BY_PARENT;
import static com.incdocs.user.dao.QueryManager.Sql.SEL_ENTITY;
import static com.incdocs.user.dao.QueryManager.Sql.SEL_RESOURCE_GROUP;

@Repository("entityDAO")
public class EntityDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("queryManager")
    private QueryManager queryManager;

    public Entity getEntity(int id) {
        return new NamedParameterJdbcTemplate(jdbcTemplate)
                .queryForObject(
                        queryManager.getSQL(SEL_ENTITY),
                        new MapSqlParameterSource("id", id),
                        (resultSet, rowCount) -> {

                            Entity parent = new Entity(resultSet.getInt("id"))
                                    .setName(resultSet.getString("name"))
                                    .setParentID(resultSet.getInt("parent_id"));

                            if (parent.getParentID() == 0) {
                                getChildEntities(parent.getEntityID())
                                        .forEach(child -> parent.addChildEntity(child));
                            }
                            return parent;
                        });
    }

    public List<Entity> getChildEntities(int parentID) {
        return new NamedParameterJdbcTemplate(jdbcTemplate)
                .query(
                        queryManager.getSQL(SEL_ENTITIES_BY_PARENT),
                        new MapSqlParameterSource("id", parentID),
                        (resultSet, rowCount) -> new Entity(resultSet.getInt("id"))
                                .setName(resultSet.getString("name"))
                                .setParentID(resultSet.getInt("parent_id"))
                );
    }

    public ResourceGroup getResourceGroup(int id) {
        return new NamedParameterJdbcTemplate(jdbcTemplate)
                .queryForObject(
                        queryManager.getSQL(SEL_RESOURCE_GROUP),
                        new MapSqlParameterSource("id", id),
                        (resultSet, rowCount) -> {
                            ResourceGroup resourceGroup = new ResourceGroup(resultSet.getInt("id"));
                            Entity entity = getEntity(resultSet.getInt("entity_id"));
                            resourceGroup.addEntity(entity);
                            while (resultSet.next()) {
                                entity = getEntity(resultSet.getInt("entity_id"));
                                resourceGroup.addEntity(entity);
                            }
                            return resourceGroup;
                        });
    }
}
