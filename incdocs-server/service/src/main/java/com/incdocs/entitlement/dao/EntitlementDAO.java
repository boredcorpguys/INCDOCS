package com.incdocs.entitlement.dao;

import com.incdocs.utils.QueryManager;
import com.incdocs.model.domain.Action;
import com.incdocs.model.domain.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.incdocs.utils.QueryManager.Sql.*;

@Repository("entitlementDAO")
public class EntitlementDAO {

    private final RowMapper<Role> roleRowMapper = (resultSet, rowCount) -> new Role(resultSet.getInt("id"))
            .setRoleName(resultSet.getString("name"))
            .setDescription(resultSet.getString("description"))
            .setClient(resultSet.getBoolean("is_client"))
            .setActive(resultSet.getBoolean("is_active"));

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("queryManager")
    private QueryManager queryManager;

    public Role getRoleInfo(int roleID) {

        return new NamedParameterJdbcTemplate(jdbcTemplate)
                .queryForObject(
                        queryManager.getSQL(SEL_ROLE),
                        new MapSqlParameterSource("id", roleID),
                        roleRowMapper);
    }

    public List<Action> getActionsForRole(int roleID) {
        return  new NamedParameterJdbcTemplate(jdbcTemplate)
                .query(
                        queryManager.getSQL(SEL_ACTIONS_FOR_ROLE),
                        new MapSqlParameterSource("id", roleID),
                        (resultSet, rowCount) ->
                            new Action(resultSet.getInt("action_id"))
                                        .setActionName(resultSet.getString("action_name"))
                                        .setDescription(resultSet.getString("action_desc")) );
    }

    public List<Role> getAllRoles() {
        return jdbcTemplate.query(
                queryManager.getSQL(SEL_ALL_ROLES),
                roleRowMapper);
    }
}
