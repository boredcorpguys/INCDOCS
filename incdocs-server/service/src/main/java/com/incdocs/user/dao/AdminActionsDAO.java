package com.incdocs.user.dao;

import com.incdocs.utils.QueryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("adminActionsDAO")
public class AdminActionsDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("queryManager")
    private QueryManager queryManager;
}
