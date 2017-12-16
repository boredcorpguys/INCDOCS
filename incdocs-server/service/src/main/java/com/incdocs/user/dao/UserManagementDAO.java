package com.incdocs.user.dao;

import model.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository("userManagementDAO")
public class UserManagementDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("queryManager")
    private QueryManager queryManager;

    public List<User> getUsers() {
        return jdbcTemplate.query("select * from INCDOCS.USERS", new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int i) throws SQLException {
                return new User()
                        .setId(rs.getInt("id"))
                        .setName(rs.getString("name"))
                        ;
            }
        });
    }


}
