package com.incdocs.user.dao;

import model.domain.Action;
import model.domain.Role;
import model.response.RoleActions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

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
                queryManager.getSQL(QueryManager.Sql.SEL_ROLE_BY_ID),
                new MapSqlParameterSource("id",roleID),
                new RowMapper<Role>() {
                    @Override
                    public Role mapRow(ResultSet rs, int i) throws SQLException {
                        return new Role()
                                .setRoleID(rs.getInt("id"))
                                .setRoleName(rs.getString("name"))
                                .setDescription(rs.getString("description"));
                    }
                });
    }

    public RoleActions getRoleActions(int roleID) {

        return new NamedParameterJdbcTemplate(jdbcTemplate)
                .queryForObject(
                        queryManager.getSQL(QueryManager.Sql.SEL_ACTIONS_FOR_ROLE),
                        new MapSqlParameterSource("id", roleID),
                        new RowMapper<RoleActions>() {
                            @Override
                            public RoleActions mapRow(ResultSet rs, int i) throws SQLException {
                                if(rs.next()){
                                    Role role = new Role().setRoleID(rs.getInt("id"))
                                            .setRoleName(rs.getString("name"))
                                            .setDescription(rs.getString("description"));
                                    RoleActions ra = new RoleActions();
                                    while(rs.next()) {
                                        Action a = new Action()
                                                .setActionID(rs.getInt("id"))
                                                .setActionName(rs.getString("name"))
                                                .setDescription(rs.getString("description"));
                                        ra.addActionForRole(role,a);
                                    }
                                    return ra;
                                }
                                return null;
                            }
                        }
                );
    }
}
