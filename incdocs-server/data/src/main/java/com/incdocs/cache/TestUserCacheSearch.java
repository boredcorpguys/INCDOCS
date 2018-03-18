package com.incdocs.cache;

import com.incdocs.model.constants.ApplicationConstants;
import com.incdocs.model.domain.User;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.Query;
import net.sf.ehcache.search.Result;
import net.sf.ehcache.search.Results;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.List;

@Component
public class TestUserCacheSearch implements InitializingBean{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AppCacheManager appCacheManager;


    @Override
    public void afterPropertiesSet() throws Exception {
        List<User> users = jdbcTemplate.query("select * from \"INCDOCS\".\"USERS\"",
                (ResultSet resultSet, int rowCount) -> {
                    User user = new User(resultSet.getString("incdocs_id"))
                            .setName(resultSet.getString("name"))
                            .setRoleID(resultSet.getInt("role_id"))
                            .setEmpID(resultSet.getString("emp_id"))
                            .setCompanyID(resultSet.getString("company_id"))
                            .setEmailID(resultSet.getString("email_id"))
                            .setContactNumber(resultSet.getString("contact_number"))
                            .setClient(resultSet.getBoolean("is_client"))
                            .setManagerID(resultSet.getString("manager_id"))
                            .setStatus(
                                    ApplicationConstants.UserStatus.fromStatus(
                                            resultSet.getString("status")));
                    return user;
                });
        users.forEach(user -> appCacheManager.put(CacheName.USER, user.getIncdocsID(), user));

        Ehcache userCache = (Ehcache) appCacheManager.getTypedCache(CacheName.USER).getNativeCache();

        Attribute<String> companyID = userCache.getSearchAttribute("company_id");
        Query query = userCache.createQuery();
        query.includeKeys();
        query.includeValues();
        query.addCriteria(companyID.ilike("x*"));
        Results results = query.execute();
        System.out.println(" Size: " + results.size());
        System.out.println("----Results-----\n");
        for (Result result : results.all()) {
            System.out.println("Got: Key[" + result.getKey()
                    + "] Value class [" + result.getValue().getClass()
                    + "] Value [" + result.getValue() + "]");
        }
    }
}
