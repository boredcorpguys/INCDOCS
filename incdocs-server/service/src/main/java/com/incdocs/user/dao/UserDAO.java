package com.incdocs.user.dao;

import com.incdocs.entitlement.dao.EntitlementDAO;
import com.incdocs.entity.dao.EntityDAO;
import com.incdocs.utils.QueryManager;
import com.incdocs.utils.Utils;
import com.indocs.model.constants.ApplicationConstants;
import com.indocs.model.domain.User;
import com.indocs.model.request.UserCreateRequest;
import com.indocs.model.request.UserProfileRequest;
import com.indocs.model.response.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;

import static com.incdocs.utils.QueryManager.Sql.*;

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

   public UserEntity getUserRolesActions(String id) {
        System.out.println("getUserRolesActions");
        return new NamedParameterJdbcTemplate(jdbcTemplate)
                .queryForObject(
                        queryManager.getSQL(SEL_USER_ENTITLEMENTS),
                        new MapSqlParameterSource("id", id),
                        (ResultSet resultSet, int rowCount) -> {
                            User user = new User(resultSet.getString("incdocs_id"))
                                    .setName(resultSet.getString("name"))
                                    .setRoleID(resultSet.getInt("role_id"))
                                    .setEmpID(resultSet.getString("emp_id"))
                                    .setCompanyID(resultSet.getString("company_id"))
                                    .setEmailID(resultSet.getString("email_id"))
                                    .setClient(resultSet.getBoolean("is_client"))
                                    .setStatus(
                                            ApplicationConstants.UserStatus.fromStatus(
                                                    resultSet.getString("status")));

                            UserEntity userRoles =
                                    new UserEntity(user).addEntity(resultSet.getString("company_id"));
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
                                    .setContactNumber(resultSet.getString("contact_number"))
                                    .setClient(resultSet.getBoolean("is_client"))
                                    .setStatus(
                                            ApplicationConstants.UserStatus.fromStatus(
                                                    resultSet.getString("status")));
                            return user;
                        }
                );
    }

    public int modifyUserDetails(UserProfileRequest user) {
        SqlParameterSource params = new MapSqlParameterSource("id", user.getId())
                .addValue("pwd", user.getPassword())
                .addValue("email", user.getEmailID())
                .addValue("contact", user.getContactNumber());
        return new NamedParameterJdbcTemplate(jdbcTemplate)
                .update(queryManager.getSQL(UPD_USER), params);
    }

    public String createUser(UserCreateRequest userCreateRequest) {
        String incdocsID = Utils.idGenerator(userCreateRequest.getCompanyID(), userCreateRequest.getId());
        jdbcTemplate.update(queryManager.getSQL(INSERT_USER),
                new Object[]{
                    userCreateRequest.getName(),
                    incdocsID,
                    userCreateRequest.getId(),
                    userCreateRequest.getRole(),
                    userCreateRequest.getCompanyID(),
                    userCreateRequest.isClient(),
                    "N",
                    userCreateRequest.getGhID()
                });
        return incdocsID;
    }
}