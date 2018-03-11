package com.incdocs.entitlement.dao;

import com.incdocs.utils.QueryManager;
import com.indocs.model.domain.Action;
import com.indocs.model.domain.Role;
import com.indocs.model.response.RoleActions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.incdocs.utils.QueryManager.Sql.*;

@Repository("entitlementDAO")
public class EntitlementDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("queryManager")
    private QueryManager queryManager;

    public Role getRoleInfo(int roleID) {

        return new NamedParameterJdbcTemplate(jdbcTemplate)
                .queryForObject(
                        queryManager.getSQL(SEL_ROLE_BY_ID),
                        new MapSqlParameterSource("id", roleID),
                        (resultSet, rowCount) -> new Role(resultSet.getInt("id"))
                                .setRoleName(resultSet.getString("name"))
                                .setDescription(resultSet.getString("description"))
                );
    }

    public RoleActions getRoleActions(int roleID) {
        final RoleActions ra = new RoleActions();
        new NamedParameterJdbcTemplate(jdbcTemplate)
                .queryForObject(
                        queryManager.getSQL(SEL_ACTIONS_FOR_ROLE),
                        new MapSqlParameterSource("id", roleID),
                        (resultSet, rowCount) -> {
                            while (resultSet.next()) {
                                if (ra.getRole() == null) {
                                    Role role = new Role(resultSet.getInt("role_id"))
                                            .setRoleName(resultSet.getString("role_name"))
                                            .setDescription(resultSet.getString("role_desc"));
                                    ra.setRole(role);
                                }
                                Action a = new Action(resultSet.getInt("action_id"))
                                        .setActionName(resultSet.getString("action_name"))
                                        .setDescription(resultSet.getString("action_desc"));
                                ra.addAction(a);
                            }
                            return ra;
                        });
        return ra;
    }

    public List<Role> getRoles() {
        return jdbcTemplate.query(
                queryManager.getSQL(SEL_ROLES),
                (resultSet, rowCount) ->
                        new Role(resultSet.getInt("id"))
                                .setRoleName(resultSet.getString("name"))
                                .setDescription(resultSet.getString("description"))

        );
    }
}
