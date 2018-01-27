package com.incdocs.user.dao;

import model.domain.Action;
import model.domain.Role;
import model.response.RoleActions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import static com.incdocs.user.dao.QueryManager.Sql.SEL_ACTIONS_FOR_ROLE;
import static com.incdocs.user.dao.QueryManager.Sql.SEL_ROLE_BY_ID;

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

        return new NamedParameterJdbcTemplate(jdbcTemplate)
                .queryForObject(
                        queryManager.getSQL(SEL_ACTIONS_FOR_ROLE),
                        new MapSqlParameterSource("id", roleID),
                        (resultSet, rowCount) -> {

                            Role role = new Role(resultSet.getInt("id"))
                                    .setRoleName(resultSet.getString("name"))
                                    .setDescription(resultSet.getString("description"));
                            RoleActions ra = new RoleActions();
                            ra.setRole(role);
                            while (resultSet.next()) {
                                Action a = new Action(resultSet.getInt("id"))
                                        .setActionName(resultSet.getString("name"))
                                        .setDescription(resultSet.getString("description"));
                                ra.addAction(a);
                            }
                            return ra;
                        });
    }
}
