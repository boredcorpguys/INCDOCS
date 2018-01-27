package com.incdocs.user.dao;

import model.domain.ResourceGroup;
import model.domain.User;
import model.response.RoleActions;
import model.response.UserRoleActionsOnEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static com.incdocs.user.dao.QueryManager.Sql.SEL_USER_ACTION_ON_ENTITIES_SQL;

@Repository("userManagementDAO")
public class UserManagementDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("queryManager")
    private QueryManager queryManager;

    @Autowired
    @Qualifier("entitlementDAO")
    private EntitlementDAO entitlementDAO;

    @Autowired
    @Qualifier("entityDAO")
    private EntityDAO entityDAO;

    public List<User> getUsers() {
        return jdbcTemplate.query("select * from INCDOCS.USERS",
                (resultSet, rowCount) -> new User(resultSet.getString("email_id"))
                        .setName(resultSet.getString("name"))

        );
    }

    public UserRoleActionsOnEntity getUserRolesActions(String emailId) {
        return new NamedParameterJdbcTemplate(jdbcTemplate)
                .queryForObject(
                        queryManager.getSQL(SEL_USER_ACTION_ON_ENTITIES_SQL),
                        new MapSqlParameterSource("id", emailId),
                        (ResultSet resultSet, int rowCount) -> {
                            User user = new User(resultSet.getString("email_id"))
                            .setName(resultSet.getString("name"))
                                    .setRoleID(resultSet.getInt("role_id"))
                                    .setEmpID(resultSet.getString("emp_id"));

                            UserRoleActionsOnEntity userRoles = new UserRoleActionsOnEntity(user);
                            RoleActions roleActions = entitlementDAO
                                    .getRoleActions(user.getRoleID());
                            userRoles.setRoleActions(roleActions);
                            ResourceGroup rg = entityDAO.getResourceGroup(resultSet.getInt("rg_id"));
                            rg.getEntities().forEach(entity -> userRoles.add(entity));
                            return userRoles;
                        }
                );
    }
}
