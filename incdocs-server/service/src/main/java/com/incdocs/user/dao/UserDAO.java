package com.incdocs.user.dao;

import model.domain.User;
import model.request.InputUser;
import model.response.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;

import static com.incdocs.user.dao.QueryManager.Sql.SEL_USER;
import static com.incdocs.user.dao.QueryManager.Sql.SEL_USER_ENTITLEMENTS;
import static com.incdocs.user.dao.QueryManager.Sql.UPD_USER;

@Repository("userManagementDAO")
public class UserDAO {

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

    public UserEntity getUserRolesActions(String incdocsID) {
        return new NamedParameterJdbcTemplate(jdbcTemplate)
                .queryForObject(
                        queryManager.getSQL(SEL_USER_ENTITLEMENTS),
                        new MapSqlParameterSource("id", incdocsID),
                        (ResultSet resultSet, int rowCount) -> {
                            User user = new User(resultSet.getString("incdocs_id"))
                                    .setName(resultSet.getString("name"))
                                    .setRoleID(resultSet.getInt("role_id"))
                                    .setEmpID(resultSet.getString("emp_id"))
                                    .setCompanyID(resultSet.getString("company_id"))
                                    .setEmailID(resultSet.getString("email_id"));

                            UserEntity userRoles =
                                    new UserEntity(user, resultSet.getString("company_id"));
                            return userRoles;
                        }
                );
    }

    public User getUser(String id) {
        return new NamedParameterJdbcTemplate(jdbcTemplate)
                .queryForObject(
                        queryManager.getSQL(SEL_USER),
                        new MapSqlParameterSource("id", id),
                        (ResultSet resultSet, int rowCount) -> {
                            User user = new User(resultSet.getString("incdocs_id"))
                                    .setName(resultSet.getString("name"))
                                    .setRoleID(resultSet.getInt("role_id"))
                                    .setEmpID(resultSet.getString("emp_id"))
                                    .setCompanyID(resultSet.getString("company_id"))
                                    .setEmailID(resultSet.getString("email_id"))
                                    .setContactNumber(resultSet.getString("contact_number"));
                            return user;
                        }
                );
    }

    public int modifyUserDetails(InputUser user) {
        SqlParameterSource params = new MapSqlParameterSource("id", user.getId())
                .addValue("pwd", user.getPassword())
                .addValue("email", user.getEmailID())
                .addValue("contact", user.getContactNumber());
        return new NamedParameterJdbcTemplate(jdbcTemplate)
                .update(queryManager.getSQL(UPD_USER), params);
    }
}
