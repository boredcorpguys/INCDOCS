package com.incdocs.user.dao;

import model.domain.Entity;
import model.domain.Role;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.incdocs.user.dao.QueryManager.Sql.*;

@Repository("entityDAO")
public class EntityDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("queryManager")
    private QueryManager queryManager;

    public Entity getEntity(String id) {
        return new NamedParameterJdbcTemplate(jdbcTemplate)
                .queryForObject(
                        queryManager.getSQL(SEL_ENTITY),
                        new MapSqlParameterSource("id", id),
                        (resultSet, rowCount) -> {

                            Entity parent = new Entity(resultSet.getString("id"))
                                    .setName(resultSet.getString("name"))
                                    .setParentID(resultSet.getString("parent_id"));

                            if (parent.getParentID() == null) {
                                getChildEntities(parent.getEntityID())
                                        .forEach(child -> parent.addChildEntity(child));
                            }
                            return parent;
                        });
    }

    public List<Entity> getChildEntities(String parentID) {
        return new NamedParameterJdbcTemplate(jdbcTemplate)
                .query(
                        queryManager.getSQL(SEL_ENTITIES_BY_PARENT),
                        new MapSqlParameterSource("id", parentID),
                        (resultSet, rowCount) -> new Entity(resultSet.getString("id"))
                                .setName(resultSet.getString("name"))
                                .setParentID(resultSet.getString("parent_id"))
                );
    }

    public List<Entity> getEntityByName(String name) {
        return new NamedParameterJdbcTemplate(jdbcTemplate)
                .query(
                        queryManager.getSQL(SEL_ENTITIES_BY_NAME),
                        new MapSqlParameterSource("name", StringUtils.join("%", name, "%")),
                        (resultSet, rowCount) -> new Entity(resultSet.getString("id"))
                                .setName(resultSet.getString("name"))
                                .setParentID(resultSet.getString("parent_id"))
                );
    }

    public List<Role> getEntityRoles(String id) {
        return new NamedParameterJdbcTemplate(jdbcTemplate)
                .query(
                queryManager.getSQL(SEL_ENTITY_ROLES),
                new MapSqlParameterSource("id", id),
                (resultSet, rowCount) ->
                        new Role(resultSet.getInt("id"))
                                .setRoleName(resultSet.getString("name"))
                                .setDescription(resultSet.getString("description"))

        );
    }
}
