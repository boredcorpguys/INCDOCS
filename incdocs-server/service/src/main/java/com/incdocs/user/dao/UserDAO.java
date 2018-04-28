package com.incdocs.user.dao;

import com.incdocs.entitlement.dao.EntitlementDAO;
import com.incdocs.entity.dao.EntityDAO;
import com.incdocs.model.constants.ApplicationConstants;
import com.incdocs.model.domain.User;
import com.incdocs.model.domain.UserEntitlement;
import com.incdocs.model.request.CreateUserRequest;
import com.incdocs.model.request.UserProfileRequest;
import com.incdocs.model.response.UserEntity;
import com.incdocs.utils.QueryManager;
import com.incdocs.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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
    private RowMapper<User> userRowMapper = (ResultSet resultSet, int rowCount) -> {
        User user = new User(resultSet.getString("incdocs_id"))
                .setName(resultSet.getString("name"))
                .setRoleID(resultSet.getInt("role_id"))
                .setEmpID(resultSet.getString("emp_id"))
                .setCompanyID(resultSet.getString("company_id"))
                .setEmailID(resultSet.getString("email_id"))
                .setContactNumber(resultSet.getString("contact_number"))
                .setClient(resultSet.getBoolean("is_client"))
                .setManagerID(resultSet.getString("manager_id"))
                .setPassword(resultSet.getString("password"))
                .setStatus(
                        ApplicationConstants.UserStatus.fromStatus(
                                resultSet.getString("status")));
        return user;
    };

    public UserEntity getUserRolesActions(String id) {
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

    public User getUser(String incdocsID) {

        return new NamedParameterJdbcTemplate(jdbcTemplate)
                .queryForObject(
                        queryManager.getSQL(SEL_USER),
                        new MapSqlParameterSource("id", incdocsID),
                        userRowMapper
                );
    }

    public List<User> getAllUsers() {
        return jdbcTemplate.query(queryManager.getSQL(SEL_ALL_USERS), userRowMapper);
    }

    public int modifyUserDetails(UserProfileRequest user) {
        SqlParameterSource params = new MapSqlParameterSource("id", user.getId())
                .addValue("pwd", user.getPassword())
                .addValue("email", user.getEmailID())
                .addValue("contact", user.getContactNumber());
        return new NamedParameterJdbcTemplate(jdbcTemplate)
                .update(queryManager.getSQL(UPD_USER), params);
    }

    public String createUser(CreateUserRequest userCreateRequest) {
        String incdocsID = Utils.idGenerator(userCreateRequest.getCompanyID(), userCreateRequest.getId());
        jdbcTemplate.update(queryManager.getSQL(INSERT_USER),
                new Object[]{
                        userCreateRequest.getName(),
                        incdocsID,
                        userCreateRequest.getId(),
                        userCreateRequest.getRoleID(),
                        userCreateRequest.getCompanyID(),
                        userCreateRequest.isClient(),
                        "N",
                        userCreateRequest.getGhID()
                });
        return incdocsID;
    }

    public UserEntitlement getUserEntitlements(String incdocsID) {
        SqlParameterSource params = new MapSqlParameterSource("id", incdocsID);
        List<String> entities = new NamedParameterJdbcTemplate(jdbcTemplate)
                .query(queryManager.getSQL(SEL_USER_ENTITLEMENTS),
                        params, (resultSet, i) -> {
                            return resultSet.getString("entity_id");
                        });
        return new UserEntitlement().setUserID(incdocsID).addEntities(entities);
    }

    public int createUserEntitlement(String incdocsID, String entityID) {
        return jdbcTemplate.update(queryManager.getSQL(INS_USER_ENTITLEMENTS),
                new Object[]{
                        incdocsID,
                        entityID
                });
    }
}
