package com.incdocs.entity.dao;

import com.incdocs.model.domain.Entity;
import com.incdocs.model.domain.Role;
import com.incdocs.model.request.CreateCompanyRequest;
import com.incdocs.utils.QueryManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static com.incdocs.utils.QueryManager.Sql.*;

@Repository("entityDAO")
public class EntityDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("queryManager")
    private QueryManager queryManager;

    private RowMapper<Entity> entityRowMapper = (resultSet, rowCount) -> {

        Entity parent = new Entity(resultSet.getString("id"))
                .setName(resultSet.getString("name"))
                .setParentID(resultSet.getString("parent_id"))
                .setGroupHeadID(resultSet.getString("gh_id"))
                .setPan(resultSet.getString("pan"));

        if (parent.getParentID() == null) {
            getChildEntities(parent.getEntityID())
                    .forEach(child -> parent.addChildEntity(child));
        }
        return parent;
    };

    public Entity getEntity(String id) {
        return new NamedParameterJdbcTemplate(jdbcTemplate)
                .queryForObject(
                        queryManager.getSQL(SEL_ENTITY),
                        new MapSqlParameterSource("id", id),
                        entityRowMapper);
    }

    public List<Entity> getAllEntities() {
        return jdbcTemplate.query(queryManager.getSQL(SEL_ALL_ENTITIES),
                entityRowMapper);
    }

    public List<Entity> getChildEntities(String parentID) {
        return new NamedParameterJdbcTemplate(jdbcTemplate)
                .query(
                        queryManager.getSQL(SEL_ENTITIES_BY_PARENT),
                        new MapSqlParameterSource("id", parentID),
                        (resultSet, rowCount) -> new Entity(resultSet.getString("id"))
                                .setName(resultSet.getString("name"))
                                .setParentID(resultSet.getString("parent_id"))
                                .setGroupHeadID(resultSet.getString("gh_id"))
                                .setPan(resultSet.getString("pan"))
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
                                .setGroupHeadID(resultSet.getString("gh_id"))
                );
    }

    public List<Role> getEntityRoles(String id) {
        return new NamedParameterJdbcTemplate(jdbcTemplate)
                .query(
                        queryManager.getSQL(SEL_ROLES_FOR_ENTITY),
                        new MapSqlParameterSource("id", id),
                        (resultSet, rowCount) ->
                                new Role(resultSet.getInt("id"))
                                        .setRoleName(resultSet.getString("name"))
                                        .setDescription(resultSet.getString("description"))

                );
    }

    public int createCompany(CreateCompanyRequest createCompanyRequest) {
        return jdbcTemplate.update(
                queryManager.getSQL(INSERT_COMPANY),
                new Object[]{
                        createCompanyRequest.getId(),
                        createCompanyRequest.getName(),
                        createCompanyRequest.getPan(),
                        true,
                        true
                });
    }

    public void createCompanyRoles(String companyID, List<Role> roles) {
        jdbcTemplate.batchUpdate(queryManager.getSQL(INSERT_COMPANY_ROLE),
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        preparedStatement.setString(1, companyID);
                        preparedStatement.setInt(2, roles.get(i).getRoleID());
                        preparedStatement.setBoolean(3, true);
                    }

                    @Override
                    public int getBatchSize() {
                        return roles.size();
                    }
                }
        );
    }


}
